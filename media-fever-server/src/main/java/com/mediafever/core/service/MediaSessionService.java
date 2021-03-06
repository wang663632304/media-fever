package com.mediafever.core.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.PushMessage;
import com.jdroid.javaweb.push.PushService;
import com.jdroid.javaweb.search.Filter;
import com.mediafever.api.exception.ServerErrorCode;
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
import com.mediafever.core.service.push.gcm.MediaSessionExpiredGcmMessage;
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
		List<Device> recipientsDevices = Lists.newArrayList();
		for (Long id : usersIds) {
			MediaSessionUser mediaSessionUser = null;
			if (id.equals(creator.getId())) {
				mediaSessionUser = new MediaSessionUser(creator, true);
			} else {
				mediaSessionUser = new MediaSessionUser(userRepository.get(id));
				recipientsDevices.addAll(mediaSessionUser.getUser().getDevices());
			}
			users.add(mediaSessionUser);
		}
		
		MediaSession mediaSession = new MediaSession(watchableTypes, date, time, users);
		mediaSessionRepository.add(mediaSession);
		
		for (Long watchableId : watchablesIds) {
			Watchable watchable = watchableService.getWatchable(watchableId);
			mediaSession.addSelection(creator, watchable);
		}
		
		pushService.send(new MediaSessionInvitationGcmMessage(creator.getFullName(), creator.getImageUrl()),
			recipientsDevices);
		return mediaSession;
	}
	
	@Transactional
	public void updateMediaSession(Long mediaSessionId, Date date, Date time, List<WatchableType> watchableTypes,
			List<Long> usersIds, Long userId) {
		MediaSession mediaSession = getMediaSelectionForEdition(mediaSessionId);
		
		List<Long> currentUsersIds = Lists.newArrayList();
		for (MediaSessionUser mediaSessionUser : mediaSession.getUsers()) {
			currentUsersIds.add(mediaSessionUser.getUser().getId());
		}
		
		List<Device> newUsersDevices = Lists.newArrayList();
		List<MediaSessionUser> newUsers = Lists.newArrayList();
		for (Long id : usersIds) {
			if (!currentUsersIds.contains(id)) {
				MediaSessionUser mediaSessionUser = new MediaSessionUser(userRepository.get(id));
				newUsers.add(mediaSessionUser);
				newUsersDevices.addAll(mediaSessionUser.getUser().getDevices());
			}
		}
		
		mediaSession.modify(watchableTypes, date, time, newUsers);
		
		// Send push notifications
		sendPushToMediaSessionUsers(mediaSession, userId, new MediaSessionUpdatedGcmMessage(mediaSession.getId()));
		User user = ApplicationContext.get().getSecurityContext().getUser();
		pushService.send(new MediaSessionInvitationGcmMessage(user.getFullName(), user.getImageUrl()), newUsersDevices);
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
		MediaSession mediaSession = getMediaSelectionForEdition(mediaSessionId);
		
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
		List<Device> recipientsDevices = Lists.newArrayList();
		for (MediaSessionUser each : mediaSession.getUsers()) {
			Long userId = each.getUser().getId();
			if (!userId.equals(excludedUserId)) {
				recipientsDevices.addAll(each.getUser().getDevices());
			}
		}
		pushService.send(pushMessage, recipientsDevices);
	}
	
	@Transactional
	public void thumbsDownMediaSelection(Long mediaSessionId, Long mediaSelectionId, Long userId) {
		MediaSession mediaSession = getMediaSelectionForEdition(mediaSessionId);
		
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
		MediaSession mediaSession = getMediaSelectionForEdition(mediaSessionId);
		
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
		List<MediaSession> mediaSessions = Lists.newArrayList();
		// Filter all the expired and not accepted media sessions
		for (MediaSession mediaSession : mediaSessionRepository.getAll(userId)) {
			if (mediaSession.isExpired()) {
				MediaSessionUser mediaSessionUser = findMediaSessionUser(mediaSession, userId);
				if ((mediaSessionUser.isAccepted() != null) && mediaSessionUser.isAccepted()) {
					mediaSessions.add(mediaSession);
				}
			} else {
				mediaSessions.add(mediaSession);
			}
		}
		return mediaSessions;
	}
	
	private MediaSessionUser findMediaSessionUser(MediaSession mediaSession, Long userId) {
		MediaSessionUser mediaSessionUser = null;
		for (MediaSessionUser each : mediaSession.getUsers()) {
			if (each.getUser().getId().equals(userId)) {
				mediaSessionUser = each;
				break;
			}
		}
		return mediaSessionUser;
	}
	
	@Transactional
	public void acceptMediaSession(Long id, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(id);
		MediaSessionUser mediaSessionUser = findMediaSessionUser(mediaSession, userId);
		mediaSessionUser.accept();
		
		// Send push notifications
		sendPushToMediaSessionUsers(mediaSession, userId, new MediaSessionUpdatedGcmMessage(mediaSession.getId()));
	}
	
	@Transactional
	public void rejectMediaSession(Long id, Long userId) {
		MediaSession mediaSession = mediaSessionRepository.get(id);
		MediaSessionUser mediaSessionUser = findMediaSessionUser(mediaSession, userId);
		mediaSession.rejectUser(mediaSessionUser);
		
		// Send push notifications
		sendPushToMediaSessionUsers(mediaSession, userId, new MediaSessionUpdatedGcmMessage(mediaSession.getId()));
	}
	
	@Transactional
	public MediaSelection addSmartSelection(Long mediaSessionId, Long userId) {
		MediaSession mediaSession = getMediaSelectionForEdition(mediaSessionId);
		
		Watchable watchable = getSmartSelection(mediaSession);
		
		return addMediaSelection(mediaSession, userId, watchable);
	}
	
	@Transactional
	public MediaSelection addRandomSelection(Long mediaSessionId, Long userId) {
		MediaSession mediaSession = getMediaSelectionForEdition(mediaSessionId);
		
		Watchable watchable = getRandomSelection(mediaSession);
		
		return addMediaSelection(mediaSession, userId, watchable);
	}
	
	@Transactional
	public MediaSelection addManualSelection(Long mediaSessionId, Long userId, Long watchableId) {
		MediaSession mediaSession = getMediaSelectionForEdition(mediaSessionId);
		
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
	
	public MediaSession getMediaSelectionForEdition(Long mediaSessionId) {
		MediaSession mediaSession = mediaSessionRepository.get(mediaSessionId);
		if (mediaSession.isExpired()) {
			// Send push notifications
			sendPushToMediaSessionUsers(mediaSession, null, new MediaSessionExpiredGcmMessage(mediaSession.getId()));
			throw ServerErrorCode.MEDIA_SESSION_EXPIRED.newBusinessException();
		}
		return mediaSession;
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
