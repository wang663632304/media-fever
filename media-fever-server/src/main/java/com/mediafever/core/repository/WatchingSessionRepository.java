package com.mediafever.core.repository;

import java.util.List;
import com.jdroid.java.repository.Repository;
import com.mediafever.core.domain.watchingsession.WatchingSession;

/**
 * Repository that handles the persistence of {@link WatchingSession}s.
 * 
 * @author Maxi Rosson
 */
public interface WatchingSessionRepository extends Repository<WatchingSession> {
	
	public List<WatchingSession> getAll(Long userId);
	
}
