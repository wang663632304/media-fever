package com.mediafever.usecase.friends;

import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.repository.FriendsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class RemoveFriendUseCase extends AbstractApiUseCase<APIService> {
	
	private FriendsRepository friendsRepository;
	private Long userId;
	private Long friendId;
	
	@Inject
	public RemoveFriendUseCase(APIService apiService, FriendsRepository friendsRepository) {
		super(apiService);
		this.friendsRepository = friendsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		getApiService().removeFriend(userId, friendId);
		friendsRepository.remove(friendId);
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}
	
}
