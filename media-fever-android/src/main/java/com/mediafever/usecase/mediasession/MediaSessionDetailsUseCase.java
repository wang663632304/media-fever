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
public class MediaSessionDetailsUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private Long mediaSessionId;
	private MediaSession mediaSession;
	private Boolean synch = true;
	
	@Inject
	public MediaSessionDetailsUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		mediaSession = mediaSessionsRepository.get(mediaSessionId);
		if (synch) {
			getApiService().getMediaSession(mediaSession);
		}
	}
	
	public MediaSession getMediaSession() {
		return mediaSession;
	}
	
	public void setMediaSessionId(Long mediaSessionId) {
		this.mediaSessionId = mediaSessionId;
	}
	
	public void setSynch(Boolean synch) {
		this.synch = synch;
	}
	
}
