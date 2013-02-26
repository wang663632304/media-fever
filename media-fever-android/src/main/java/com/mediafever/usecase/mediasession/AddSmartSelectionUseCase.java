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
public class AddSmartSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private MediaSession mediaSession;
	private MediaSelection mediaSelection;
	
	@Inject
	public AddSmartSelectionUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		mediaSelection = getApiService().addSmartSelection(mediaSession);
		mediaSession.addSelection(mediaSelection);
		mediaSessionsRepository.update(mediaSession);
	}
	
	/**
	 * @param mediaSession the mediaSession to set
	 */
	public void setMediaSession(MediaSession mediaSession) {
		this.mediaSession = mediaSession;
	}
	
	/**
	 * @return the mediaSelection
	 */
	public MediaSelection getMediaSelection() {
		return mediaSelection;
	}
	
}
