package com.mediafever.usecase;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.watchingsession.WatchingSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionsUseCase extends AbstractApiUseCase<APIService> {
	
	private Long userId;
	private List<WatchingSession> pendingWatchingSessions = Lists.newArrayList();
	private List<WatchingSession> acceptedWatchingSessions = Lists.newArrayList();
	
	@Inject
	public WatchingSessionsUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		List<WatchingSession> watchingSessions = getApiService().getWatchingSessions(userId);
		for (WatchingSession watchingSession : watchingSessions) {
			if (watchingSession.isAccepted()) {
				acceptedWatchingSessions.add(watchingSession);
			} else {
				pendingWatchingSessions.add(watchingSession);
			}
		}
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<WatchingSession> getPendingWatchingSessions() {
		return pendingWatchingSessions;
	}
	
	public List<WatchingSession> getAcceptedWatchingSessions() {
		return acceptedWatchingSessions;
	}
}
