package com.mediafever.core.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mediafever.core.domain.session.MediaSession;
import com.mediafever.core.domain.session.MediaSessionUser;
import com.mediafever.core.repository.MediaSessionRepository;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class MediaSessionService {
	
	@Autowired
	private MediaSessionRepository mediaSessionRepository;
	
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
