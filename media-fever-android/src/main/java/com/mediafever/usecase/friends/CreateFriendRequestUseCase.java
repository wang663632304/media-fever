package com.mediafever.usecase.friends;

import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.UserImpl;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class CreateFriendRequestUseCase extends AbstractApiUseCase<APIService> {
	
	private UserImpl user;
	
	@Inject
	public CreateFriendRequestUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		FriendRequest friendRequest = new FriendRequest(null, user, (UserImpl)SecurityContext.get().getUser());
		getApiService().createFriendRequest(friendRequest);
	}
	
	public void setUser(UserImpl user) {
		this.user = user;
	}
}
