package com.mediafever.repository;

import java.util.List;
import com.jdroid.android.repository.SynchronizedRepository;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public interface MediaSessionsRepository extends SynchronizedRepository<MediaSession> {
	
	public List<MediaSession> getPendingMediaSessions();
	
	public List<MediaSession> getAcceptedMediaSessions();
	
}
