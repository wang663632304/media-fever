package com.mediafever.usecase;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class AcceptMediaSessionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSession mediaSession;
	private Boolean accept;
	
	@Inject
	public AcceptMediaSessionUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		// TODO Implement this
	}
	
	public void setAsAccepted() {
		accept = true;
	}
	
	public void setAsRejected() {
		accept = false;
	}
	
	public void setMediaSession(MediaSession mediaSession) {
		this.mediaSession = mediaSession;
	}
	
}
