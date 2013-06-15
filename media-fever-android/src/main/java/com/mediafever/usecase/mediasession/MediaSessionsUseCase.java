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
		mediaSessionsRepository.getAll();
	}
	
	// TODO This three methods are called on the UI thread, and perform a request
	public List<MediaSession> getPendingMediaSessions() {
		return mediaSessionsRepository.getPendingMediaSessions();
	}
	
	public List<MediaSession> getActiveMediaSessions() {
		return mediaSessionsRepository.getActiveMediaSessions();
	}
	
	public List<MediaSession> getExpiredMediaSessions() {
		return mediaSessionsRepository.getExpiredMediaSessions();
	}
}
