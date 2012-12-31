package com.mediafever.core.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.javaweb.push.PushService;
import com.jdroid.javaweb.search.Filter;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.User;
import com.mediafever.core.domain.session.MediaSession;
import com.mediafever.core.domain.session.MediaSessionUser;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.domain.watchable.WatchableType;
import com.mediafever.core.repository.CustomFilterKey;
import com.mediafever.core.repository.MediaSessionRepository;
import com.mediafever.core.repository.UserRepository;
import com.mediafever.core.service.push.gcm.MediaSessionInvitationGcmMessage;

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
	public MediaSession createMediaSession(Date date, Date time, List<WatchableType> watchableTypes, List<Long> usersIds) {
		
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
		
		List<Long> recipientsIds = Lists.newArrayList(usersIds);
		recipientsIds.remove(creator.getId());
		if (CollectionUtils.isNotEmpty(recipientsIds)) {
			pushService.send(new MediaSessionInvitationGcmMessage(creator.getFullName(), creator.getImageUrl()),
				recipientsIds);
		}
		return mediaSession;
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
	
	public Watchable getSmartSelection(Long id) {
		
		MediaSession mediaSession = mediaSessionRepository.get(id);
		
		// TODO Use the SMART Selection algorithm here
		Filter filter = new Filter(1, 1000);
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, mediaSession.getWatchableTypes());
		List<Watchable> watchables = watchableService.searchWatchable(filter).getData();
		int random = IdGenerator.getRandomIntId() % watchables.size();
		return watchables.get(random);
	}
}
