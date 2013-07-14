package com.mediafever.usecase.friends;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.FacebookUser;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookFriendsUseCase extends AbstractApiUseCase<APIService> {
	
	private Long userId;
	private List<FacebookUser> friends;
	
	@Inject
	public FacebookFriendsUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		friends = getApiService().getFacebookFriends(userId);
	}
	
	public List<FacebookUser> getFriends() {
		return friends;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
