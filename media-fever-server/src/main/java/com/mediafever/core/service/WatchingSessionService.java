package com.mediafever.core.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mediafever.core.domain.watchingsession.WatchingSession;
import com.mediafever.core.repository.WatchingSessionRepository;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class WatchingSessionService {
	
	@Autowired
	private WatchingSessionRepository watchingSessionRepository;
	
	public List<WatchingSession> getAll(Long userId) {
		return watchingSessionRepository.getAll(userId);
	}
}
