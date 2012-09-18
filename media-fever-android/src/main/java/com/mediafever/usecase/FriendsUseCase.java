package com.mediafever.usecase;

import java.util.List;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.domain.UserImpl;
import com.mediafever.repository.FriendsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendsUseCase extends AbstractApiUseCase<APIService> {
	
	private FriendsRepository friendsRepository;
	private Long userId;
	
	@Inject
	public FriendsUseCase(APIService apiService, FriendsRepository friendsRepository) {
		super(apiService);
		this.friendsRepository = friendsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (friendsRepository.isOutdated()) {
			List<UserImpl> friends = getApiService().getFriends(userId);
			friendsRepository.replaceAll(friends);
		}
	}
	
	public List<UserImpl> getFriends() {
		return friendsRepository.getAll();
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
