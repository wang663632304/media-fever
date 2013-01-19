package com.mediafever.repository;

import com.jdroid.android.repository.SynchronizedInMemoryRepository;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class InMemoryFriendRequestsRepository extends SynchronizedInMemoryRepository<FriendRequest> implements
		FriendRequestsRepository {
	
	/**
	 * @see com.mediafever.repository.FriendRequestsRepository#getBySender(com.mediafever.domain.UserImpl)
	 */
	@Override
	public FriendRequest getBySender(UserImpl sender) {
		for (FriendRequest friendRequest : getAll()) {
			if (friendRequest.getSender().equals(sender)) {
				return friendRequest;
			}
		}
		return null;
	}
	
}
