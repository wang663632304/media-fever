package com.mediafever.usecase.friends;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.FriendRequest;
import com.mediafever.repository.FriendRequestsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestsUseCase extends AbstractApiUseCase<APIService> {
	
	private FriendRequestsRepository friendRequestsRepository;
	private Long userId;
	
	@Inject
	public FriendRequestsUseCase(APIService apiService, FriendRequestsRepository friendRequestsRepository) {
		super(apiService);
		this.friendRequestsRepository = friendRequestsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (friendRequestsRepository.isOutdated()) {
			List<FriendRequest> friendRequests = getApiService().getFriendRequests(userId);
			friendRequestsRepository.replaceAll(friendRequests);
			friendRequestsRepository.resetLastUpdateTimestamp();
		}
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<FriendRequest> getFriendRequests() {
		return friendRequestsRepository.getAll();
	}
}
