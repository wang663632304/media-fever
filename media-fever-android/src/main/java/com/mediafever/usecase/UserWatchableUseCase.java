package com.mediafever.usecase;

import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class UserWatchableUseCase extends AbstractApiUseCase<APIService> {
	
	private Long userId;
	private Long watchableId;
	private UserWatchable<Watchable> userWatchable;
	
	@Inject
	public UserWatchableUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		userWatchable = getApiService().getUserWatchable(userId, watchableId);
	}
	
	/**
	 * @return the userWatchable
	 */
	public UserWatchable<Watchable> getUserWatchable() {
		return userWatchable;
	}
	
	public void setWatchableId(Long watchableId) {
		this.watchableId = watchableId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
