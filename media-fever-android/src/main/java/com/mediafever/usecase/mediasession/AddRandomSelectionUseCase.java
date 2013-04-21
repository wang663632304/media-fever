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
public class AddRandomSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private Long mediaSessionId;
	private MediaSelection mediaSelection;
	
	@Inject
	public AddRandomSelectionUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		MediaSession mediaSession = mediaSessionsRepository.get(mediaSessionId);
		mediaSelection = getApiService().addRandomSelection(mediaSession);
		mediaSession.addSelection(mediaSelection);
		mediaSessionsRepository.update(mediaSession);
	}
	
	public void setMediaSessionId(Long mediaSessionId) {
		this.mediaSessionId = mediaSessionId;
	}
	
	/**
	 * @return the mediaSelection
	 */
	public MediaSelection getMediaSelection() {
		return mediaSelection;
	}
	
}
