package com.mediafever.core.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.javaweb.push.PushMessage;
import com.jdroid.javaweb.push.PushService;
import com.jdroid.javaweb.search.Filter;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.User;
import com.mediafever.core.domain.session.MediaSelection;
import com.mediafever.core.domain.session.MediaSession;
import com.mediafever.core.domain.session.MediaSessionUser;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.domain.watchable.WatchableType;
import com.mediafever.core.repository.CustomFilterKey;
import com.mediafever.core.repository.MediaSessionRepository;
import com.mediafever.core.repository.UserRepository;
import com.mediafever.core.service.push.gcm.MediaSelectionAddedGcmMessage;
import com.mediafever.core.service.push.gcm.MediaSelectionRemovedGcmMessage;
import com.mediafever.core.service.push.gcm.MediaSelectionThumbsDownGcmMessage;
import com.mediafever.core.service.push.gcm.MediaSelectionThumbsUpGcmMessage;
import com.mediafever.core.service.push.gcm.MediaSessionInvitationGcmMessage;
import com.mediafever.core.service.push.gcm.MediaSessionLeftGcmMessage;
import com.mediafever.core.service.push.gcm.MediaSessionUpdatedGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class MediaSessionService {
	
	@Autowired
	private MediaSessionRepository mediaSessionRepository;
	
	@Autowired
	private WatchableService watchableService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PushService pushService;
	
	@Transactional
	public MediaSession createMediaSession(Date date, Date time, List<WatchableType> watchableTypes,
			List<Long> usersIds, List<Long> watchablesIds) {
		
		User creator = ApplicationContext.get().getSecurityContext().getUser();
		List<MediaSessionUser> users = Lists.newArrayList();
		for (Long id : usersIds) {
			if (id.equals(creator.getId())) {
				users.add(new MediaSessionUser(creator, true));
			} else {
				users.add(new MediaSessionUser(userRepository.get(id)));
			}
		}
		
		MediaSession mediaSession = new MediaSession(watchableTypes, date, time, users);
		mediaSessionRepository.add(mediaSession);
		
		for (Long watchableId : watchablesIds) {
			Watchable watchable = watchableService.getWatchable(watchableId);
			mediaSession.addSelection(creator, watchable);
		}
		
		List<Long> recipientsIds = Lists.newArrayList(usersIds);
		recipientsIds.remove(creator.getId());
		if (CollectionUtils.isNotEmpty(recipientsIds)) {
			pushService.send(new MediaSessionInvitationGcmMessage(creator.getFullName(), creator.getImageUrl()),
				recipientsIds);
		}
		return mediaSession;
	}
	
	@Transactional
	public void updateMediaSession(Long mediaSessionId, Date date, Date time, List<WatchableType> watchableTypes,
			List<Long> usersIds, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(mediaSessionId);
		mediaSession.checkExpiration();
		
		List<Long> currentUsersIds = Lists.newArrayList();
		for (MediaSessionUser mediaSessionUser : mediaSession.getUsers()) {
			currentUsersIds.add(mediaSessionUser.getUser().getId());
		}
		
		List<MediaSessionUser> newUsers = Lists.newArrayList();
		for (Long id : usersIds) {
			if (!currentUsersIds.contains(id)) {
				newUsers.add(new MediaSessionUser(userRepository.get(id)));
			}
		}
		
		mediaSession.modify(watchableTypes, date, time, newUsers);
		
		// Send push notifications
		sendPushToMediaSessionUsers(mediaSession, userId, new MediaSessionUpdatedGcmMessage(mediaSession.getId()));
	}
	
	@Transactional
	public void leaveMediaSession(Long mediaSessionId, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(mediaSessionId);
		User user = userRepository.get(userId);
		mediaSession.leave(user);
		
		// If all the users left the media session, we remove it
		if (mediaSession.getUsers().isEmpty()) {
			mediaSessionRepository.remove(mediaSession);
		}
		
		// Send push notifications
		sendPushToMediaSessionUsers(mediaSession, userId,
			new MediaSessionLeftGcmMessage(mediaSession.getId(), user.getFullName(), user.getImageUrl()));
	}
	
	@Transactional
	public void thumbsUpMediaSelection(Long mediaSessionId, Long mediaSelectionId, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(mediaSessionId);
		mediaSession.checkExpiration();
		
		User user = userRepository.get(userId);
		MediaSelection mediaSelection = findMediaSelection(mediaSelectionId, mediaSession);
		if (mediaSelection != null) {
			mediaSession.thumbsUp(mediaSelection, user);
			
			// Send push notifications
			sendPushToMediaSessionUsers(mediaSession, userId,
				new MediaSelectionThumbsUpGcmMessage(mediaSession.getId(), mediaSelection.getWatchable().getName(),
						user.getFullName(), user.getImageUrl()));
		}
	}
	
	private void sendPushToMediaSessionUsers(MediaSession mediaSession, Long excludedUserId, PushMessage pushMessage) {
		List<Long> recipientsIds = Lists.newArrayList();
		for (MediaSessionUser each : mediaSession.getUsers()) {
			Long userId = each.getUser().getId();
			if (!userId.equals(excludedUserId)) {
				recipientsIds.add(userId);
			}
		}
		if (CollectionUtils.isNotEmpty(recipientsIds)) {
			pushService.send(pushMessage, recipientsIds);
		}
	}
	
	@Transactional
	public void thumbsDownMediaSelection(Long mediaSessionId, Long mediaSelectionId, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(mediaSessionId);
		mediaSession.checkExpiration();
		
		User user = userRepository.get(userId);
		MediaSelection mediaSelection = findMediaSelection(mediaSelectionId, mediaSession);
		if (mediaSelection != null) {
			mediaSession.thumbsDown(mediaSelection, user);
			
			// Send push notifications
			sendPushToMediaSessionUsers(mediaSession, userId,
				new MediaSelectionThumbsDownGcmMessage(mediaSession.getId(), mediaSelection.getWatchable().getName(),
						user.getFullName(), user.getImageUrl()));
		}
	}
	
	@Transactional
	public void removeMediaSelection(Long mediaSessionId, Long mediaSelectionId, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(mediaSessionId);
		mediaSession.checkExpiration();
		
		MediaSelection mediaSelection = findMediaSelection(mediaSelectionId, mediaSession);
		if (mediaSelection != null) {
			mediaSession.removeSelection(mediaSelection);
			
			// Send push notifications
			User user = userRepository.get(userId);
			sendPushToMediaSessionUsers(mediaSession, userId, new MediaSelectionRemovedGcmMessage(mediaSession.getId(),
					mediaSelection.getWatchable().getName(), user.getFullName(), user.getImageUrl()));
		}
	}
	
	private MediaSelection findMediaSelection(Long mediaSelectionId, MediaSession mediaSession) {
		MediaSelection mediaSelection = null;
		for (MediaSelection each : mediaSession.getSelections()) {
			if (each.getId().equals(mediaSelectionId)) {
				mediaSelection = each;
				break;
			}
		}
		return mediaSelection;
	}
	
	public MediaSession get(Long mediaSessionId) {
		return mediaSessionRepository.get(mediaSessionId);
	}
	
	public List<MediaSession> getAll(Long userId) {
		return mediaSessionRepository.getAll(userId);
	}
	
	@Transactional
	public void acceptMediaSession(Long id, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(id);
		for (MediaSessionUser mediaSessionUser : mediaSession.getUsers()) {
			if (mediaSessionUser.getUser().getId().equals(userId)) {
				mediaSessionUser.accept();
				break;
			}
		}
	}
	
	@Transactional
	public void rejectMediaSession(Long id, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(id);
		for (MediaSessionUser mediaSessionUser : mediaSession.getUsers()) {
			if (mediaSessionUser.getUser().getId().equals(userId)) {
				mediaSessionUser.reject();
				break;
			}
		}
	}
	
	@Transactional
	public MediaSelection addSmartSelection(Long id, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(id);
		mediaSession.checkExpiration();
		
		Watchable watchable = getSmartSelection(mediaSession);
		
		return addMediaSelection(mediaSession, userId, watchable);
	}
	
	@Transactional
	public MediaSelection addRandomSelection(Long id, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(id);
		mediaSession.checkExpiration();
		
		Watchable watchable = getRandomSelection(mediaSession);
		
		return addMediaSelection(mediaSession, userId, watchable);
	}
	
	@Transactional
	public MediaSelection addManualSelection(Long id, Long userId, Long watchableId) {
		MediaSession mediaSession = mediaSessionRepository.get(id);
		mediaSession.checkExpiration();
		
		Watchable watchable = watchableService.getWatchable(watchableId);
		
		return addMediaSelection(mediaSession, userId, watchable);
	}
	
	private MediaSelection addMediaSelection(MediaSession mediaSession, Long userId, Watchable watchable) {
		User user = userRepository.get(userId);
		MediaSelection mediaSelection = mediaSession.addSelection(user, watchable);
		
		// Send push notifications
		sendPushToMediaSessionUsers(mediaSession, userId, new MediaSelectionAddedGcmMessage(mediaSession.getId(),
				mediaSelection.getWatchable().getName(), user.getFullName(), user.getImageUrl()));
		
		return mediaSelection;
	}
	
	private Watchable getRandomSelection(MediaSession mediaSession) {
		
		// Pick a random movie or series between the last 10.000 movies or series released
		Filter filter = new Filter(1, 10000);
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, mediaSession.getWatchableTypes());
		List<Watchable> watchables = watchableService.searchWatchable(filter).getData();
		int random = IdGenerator.getRandomIntId() % watchables.size();
		Watchable watchable = watchables.get(random);
		
		// Retry if the selection is duplicated
		for (MediaSelection mediaSelection : mediaSession.getSelections()) {
			if (mediaSelection.getWatchable().equals(watchable)) {
				watchable = getSmartSelection(mediaSession);
				break;
			}
		}
		return watchable;
	}
	
	private Watchable getSmartSelection(MediaSession mediaSession) {
		
		// TODO Replace with the SMART Selection algorithm here
		Filter filter = new Filter(1, 1000);
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, mediaSession.getWatchableTypes());
		List<Watchable> watchables = watchableService.searchWatchable(filter).getData();
		int random = IdGenerator.getRandomIntId() % watchables.size();
		Watchable watchable = watchables.get(random);
		// /////////////////////////////////////////////
		
		// Retry if the selection is duplicated
		for (MediaSelection mediaSelection : mediaSession.getSelections()) {
			if (mediaSelection.getWatchable().equals(watchable)) {
				watchable = getSmartSelection(mediaSession);
				break;
			}
		}
		return watchable;
	}
}
