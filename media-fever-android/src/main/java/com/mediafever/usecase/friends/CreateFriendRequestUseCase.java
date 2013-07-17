package com.mediafever.usecase.friends;

import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.FriendRequest;
import com.mediafever.repository.FriendRequestsRepository;
import com.mediafever.repository.FriendsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class CreateFriendRequestUseCase extends AbstractApiUseCase<APIService> {
	
	private FriendsRepository friendsRepository;
	private FriendRequestsRepository friendRequestsRepository;
	private Long userId;
	private Boolean addAsFriend;
	
	@Inject
	public CreateFriendRequestUseCase(APIService apiService, FriendsRepository friendsRepository,
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
		
		addAsFriend = false;
		
		getApiService().createFriendRequest(userId, SecurityContext.get().getUser().getId());
		
		// If I have a pending friend request of the user I am trying to invite, add it as a friend and remove the
		// friend request
		FriendRequest friendRequest = friendRequestsRepository.getBySender(userId);
		if (friendRequest != null) {
			friendRequestsRepository.remove(friendRequest);
			friendsRepository.add(friendRequest.getSender());
			addAsFriend = true;
		}
	}
	
	public Boolean wasAddAsFriend() {
		return addAsFriend;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
