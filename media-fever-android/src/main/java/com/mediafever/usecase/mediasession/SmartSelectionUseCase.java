package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class SmartSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSession mediaSession;
	private Watchable watchable;
	
	@Inject
	public SmartSelectionUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		watchable = getApiService().getSmartSelection(mediaSession.getId());
	}
	
	/**
	 * @param mediaSession the mediaSession to set
	 */
	public void setMediaSession(MediaSession mediaSession) {
		this.mediaSession = mediaSession;
	}
	
	/**
	 * @return the watchable
	 */
	public Watchable getWatchable() {
		return watchable;
	}
	
}
