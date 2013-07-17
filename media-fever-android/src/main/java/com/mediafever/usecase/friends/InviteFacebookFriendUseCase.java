package com.mediafever.usecase.friends;

import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.FacebookUser;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class InviteFacebookFriendUseCase extends AbstractApiUseCase<APIService> {
	
	private FacebookUser facebookUser;
	
	@Inject
	public InviteFacebookFriendUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		getApiService().inviteFacebookFriend(SecurityContext.get().getUser().getId(), facebookUser);
	}
	
	/**
	 * @param facebookUser the facebookUser to set
	 */
	public void setFacebookUser(FacebookUser facebookUser) {
		this.facebookUser = facebookUser;
	}
}
