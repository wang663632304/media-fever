package com.mediafever.usecase.friends;

import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.UserImpl;
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
	private UserImpl user;
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
		
		FriendRequest friendRequest = new FriendRequest(null, user, (UserImpl)SecurityContext.get().getUser());
		getApiService().createFriendRequest(friendRequest);
		
		// If I have a pending friend request of the user I am trying to invite, add it as a friend and remove the
		// friend request
		friendRequest = friendRequestsRepository.getBySender(user);
		if (friendRequest != null) {
			friendRequestsRepository.remove(friendRequest);
			friendsRepository.add(user);
			addAsFriend = true;
		}
	}
	
	public void setUser(UserImpl user) {
		this.user = user;
	}
	
	public UserImpl getUser() {
		return user;
	}
	
	public Boolean wasAddAsFriend() {
		return addAsFriend;
	}
}
