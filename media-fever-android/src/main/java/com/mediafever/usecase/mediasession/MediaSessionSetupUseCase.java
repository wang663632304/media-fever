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
public class MediaSessionSetupUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private MediaSession mediaSession;
	private Boolean created;
	
	@Inject
	public MediaSessionSetupUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (mediaSession.getId() != null) {
			getApiService().updateMediaSession(mediaSession);
			mediaSessionsRepository.update(mediaSession);
			created = false;
		} else {
			mediaSession = getApiService().createMediaSession(mediaSession);
			mediaSessionsRepository.add(mediaSession);
			created = true;
		}
	}
	
	public void setMediaSession(MediaSession mediaSession) {
		this.mediaSession = mediaSession;
	}
	
	public MediaSession getMediaSession() {
		return mediaSession;
	}
	
	public Boolean isCreated() {
		return created;
	}
}
