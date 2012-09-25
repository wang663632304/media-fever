package com.mediafever.core.repository;

import java.util.List;
import com.jdroid.java.repository.Repository;
import com.mediafever.core.domain.session.MediaSession;

/**
 * Repository that handles the persistence of {@link MediaSession}s.
 * 
 * @author Maxi Rosson
 */
public interface MediaSessionRepository extends Repository<MediaSession> {
	
	public List<MediaSession> getAll(Long userId);
	
}
