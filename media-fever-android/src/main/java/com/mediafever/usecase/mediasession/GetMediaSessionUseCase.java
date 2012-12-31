package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class GetMediaSessionUseCase extends AbstractApiUseCase<APIService> {
	
	private Long mediaSessionId;
	private MediaSession mediaSession;
	
	@Inject
	public GetMediaSessionUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		mediaSession = getApiService().getMediaSession(mediaSessionId);
	}
	
	public MediaSession getMediaSession() {
		return mediaSession;
	}
	
	public void setMediaSessionId(Long mediaSessionId) {
		this.mediaSessionId = mediaSessionId;
	}
	
}
