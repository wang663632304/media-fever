package com.mediafever.usecase;

import java.util.List;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.domain.watchingsession.WatchingSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionsUseCase extends AbstractApiUseCase<APIService> {
	
	private Long userId;
	private List<WatchingSession> watchingSessions;
	
	@Inject
	public WatchingSessionsUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		watchingSessions = getApiService().getWatchingSessions(userId);
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<WatchingSession> getWatchingSessions() {
		return watchingSessions;
	}
}
