package com.mediafever.usecase.mediasession;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionsUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	
	private List<MediaSession> pendingMediaSessions;
	private List<MediaSession> activeMediaSessions;
	private List<MediaSession> expiredMediaSessions;
	
	@Inject
	public MediaSessionsUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		pendingMediaSessions = mediaSessionsRepository.getPendingMediaSessions();
		activeMediaSessions = mediaSessionsRepository.getActiveMediaSessions();
		expiredMediaSessions = mediaSessionsRepository.getExpiredMediaSessions();
	}
	
	public List<MediaSession> getPendingMediaSessions() {
		return pendingMediaSessions;
	}
	
	public List<MediaSession> getActiveMediaSessions() {
		return activeMediaSessions;
	}
	
	public List<MediaSession> getExpiredMediaSessions() {
		return expiredMediaSessions;
	}
}
