package com.mediafever.usecase.friends;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.FriendRequest;
import com.mediafever.repository.FriendRequestsRepository;
import com.mediafever.repository.FriendsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class AcceptFriendRequestUseCase extends AbstractApiUseCase<APIService> {
	
	private FriendRequestsRepository friendRequestsRepository;
	private FriendsRepository friendsRepository;
	private FriendRequest friendRequest;
	private Boolean accept;
	
	@Inject
	public AcceptFriendRequestUseCase(APIService apiService, FriendsRepository friendsRepository,
			FriendRequestsRepository friendRequestsRepository) {
		super(apiService);
		this.friendsRepository = friendsRepository;
		this.friendRequestsRepository = friendRequestsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (accept) {
			getApiService().acceptFriendRequest(friendRequest);
			friendsRepository.add(friendRequest.getSender());
		} else {
			getApiService().rejectFriendRequest(friendRequest);
		}
		friendRequestsRepository.remove(friendRequest);
	}
	
	public void setFriendRequest(FriendRequest friendRequest) {
		this.friendRequest = friendRequest;
	}
	
	public void setAsAccepted() {
		accept = true;
	}
	
	public void setAsRejected() {
		accept = false;
	}
	
	public FriendRequest getFriendRequest() {
		return friendRequest;
	}
	
}
