package com.mediafever.usecase;

import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.domain.watchingsession.WatchingSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class AcceptWatchingSessionUseCase extends AbstractApiUseCase<APIService> {
	
	private WatchingSession watchingSession;
	private Boolean accept;
	
	@Inject
	public AcceptWatchingSessionUseCase(APIService apiService) {
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
	
	public void setWatchingSession(WatchingSession watchingSession) {
		this.watchingSession = watchingSession;
	}
	
}
