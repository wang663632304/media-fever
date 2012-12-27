package com.mediafever.repository;

import java.util.Collections;
import java.util.List;
import com.jdroid.android.repository.SynchronizedInMemoryRepository;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class InMemoryMediaSessionsRepository extends SynchronizedInMemoryRepository<MediaSession> implements
		MediaSessionsRepository {
	
	/**
	 * @see com.mediafever.repository.MediaSessionsRepository#getPendingMediaSessions()
	 */
	@Override
	public List<MediaSession> getPendingMediaSessions() {
		List<MediaSession> pendingMediaSessions = Lists.newArrayList();
		for (MediaSession mediaSession : getAll()) {
			if (!mediaSession.isAccepted()) {
				pendingMediaSessions.add(mediaSession);
			}
		}
		Collections.sort(pendingMediaSessions);
		return pendingMediaSessions;
	}
	
	/**
	 * @see com.mediafever.repository.MediaSessionsRepository#getAcceptedMediaSessions()
	 */
	@Override
	public List<MediaSession> getAcceptedMediaSessions() {
		List<MediaSession> acceptedMediaSessions = Lists.newArrayList();
		for (MediaSession mediaSession : getAll()) {
			if (mediaSession.isAccepted()) {
				acceptedMediaSessions.add(mediaSession);
			}
		}
		Collections.sort(acceptedMediaSessions);
		return acceptedMediaSessions;
	}
}
