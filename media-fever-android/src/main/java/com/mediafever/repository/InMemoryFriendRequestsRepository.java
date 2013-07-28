package com.mediafever.repository;

import com.jdroid.android.repository.SynchronizedInMemoryRepository;
import com.mediafever.domain.FriendRequest;

/**
 * 
 * @author Maxi Rosson
 */
public class InMemoryFriendRequestsRepository extends SynchronizedInMemoryRepository<FriendRequest> implements
		FriendRequestsRepository {
	
	/**
	 * @see com.mediafever.repository.FriendRequestsRepository#getBySender(java.lang.Long)
	 */
	@Override
	public FriendRequest getBySender(Long senderId) {
		for (FriendRequest friendRequest : getAll()) {
			if (friendRequest.getSender().getId().equals(senderId)) {
				return friendRequest;
			}
		}
		return null;
	}
	
}
