package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class RemoveMediaSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private Long mediaSessionId;
	private MediaSelection mediaSelection;
	
	@Inject
	public RemoveMediaSelectionUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		MediaSession mediaSession = mediaSessionsRepository.get(mediaSessionId);
		getApiService().removeMediaSelection(mediaSession, mediaSelection);
		mediaSession.removeSelection(mediaSelection);
		mediaSessionsRepository.update(mediaSession);
	}
	
	/**
	 * @param mediaSelection the mediaSelection to set
	 */
	public void setMediaSelection(MediaSelection mediaSelection) {
		this.mediaSelection = mediaSelection;
	}
	
	public void setMediaSessionId(Long mediaSessionId) {
		this.mediaSessionId = mediaSessionId;
	}
}
