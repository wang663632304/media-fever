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
public class UpdateUserWatchableUseCase extends AbstractApiUseCase<APIService> {
	
	private UserWatchable<Watchable> userWatchable;
	private Boolean watched;
	private Boolean isInWishList;
	
	@Inject
	public UpdateUserWatchableUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		userWatchable.modify(watched, isInWishList);
		if (userWatchable.getId() != null) {
			getApiService().updateUserWatchable(userWatchable);
		} else {
			userWatchable = getApiService().createUserWatchable(userWatchable);
		}
		// TODO If this call fails, we should rollback the userWatchable state
	}
	
	public void toogleWatched() {
		watched = !userWatchable.isWatched();
	}
	
	public void toogleIsInWishList() {
		isInWishList = !userWatchable.isInWishList();
	}
	
	public void setUserWatchable(UserWatchable<Watchable> userWatchable) {
		this.userWatchable = userWatchable;
		watched = userWatchable.isWatched();
		isInWishList = userWatchable.isInWishList();
	}
	
	public UserWatchable<Watchable> getUserWatchable() {
		return userWatchable;
	}
}
