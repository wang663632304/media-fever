package com.mediafever.core.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mediafever.core.domain.session.MediaSession;
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
}
