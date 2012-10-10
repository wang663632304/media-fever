package com.mediafever.core.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.java.collections.Lists;
import com.jdroid.javaweb.push.PushService;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.User;
import com.mediafever.core.domain.session.MediaSession;
import com.mediafever.core.domain.session.MediaSessionUser;
import com.mediafever.core.domain.watchable.WatchableType;
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
	private UserRepository userRepository;
	
	@Autowired
	private PushService pushService;
	
	@Transactional
	public void createMediaSession(Date date, List<WatchableType> watchableTypes, List<Long> usersIds) {
		List<MediaSessionUser> users = Lists.newArrayList();
		for (Long id : usersIds) {
			users.add(new MediaSessionUser(userRepository.get(id)));
		}
		User creator = ApplicationContext.get().getSecurityContext().getUser();
		users.add(new MediaSessionUser(creator, true));
		
		MediaSession mediaSession = new MediaSession(watchableTypes, date, users);
		mediaSessionRepository.add(mediaSession);
		
		pushService.send(new MediaSessionInvitationGcmMessage(creator.getFullName(), creator.getImageUrl()), usersIds);
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
}
