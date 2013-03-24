package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class AcceptMediaSessionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private Long mediaSessionId;
	private Boolean accept;
	
	@Inject
	public AcceptMediaSessionUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		MediaSession mediaSession = mediaSessionsRepository.get(mediaSessionId);
		if (accept) {
			getApiService().acceptMediaSession(mediaSession);
			mediaSession.accept();
			mediaSessionsRepository.update(mediaSession);
		} else {
			getApiService().rejectMediaSession(mediaSession);
			mediaSessionsRepository.remove(mediaSession);
		}
	}
	
	public void setAsAccepted() {
		accept = true;
	}
	
	public void setAsRejected() {
		accept = false;
	}
	
	public void setMediaSessionId(Long mediaSessionId) {
		this.mediaSessionId = mediaSessionId;
	}
	
}
