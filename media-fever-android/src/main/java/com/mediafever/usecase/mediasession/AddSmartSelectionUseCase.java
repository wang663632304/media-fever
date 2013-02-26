package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class AddSmartSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSession mediaSession;
	private MediaSelection mediaSelection;
	
	@Inject
	public AddSmartSelectionUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		mediaSelection = getApiService().addSmartSelection(mediaSession);
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
