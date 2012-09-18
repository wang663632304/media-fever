package com.mediafever.repository;

import com.jdroid.android.repository.SynchronizedInMemoryRepository;
import com.mediafever.domain.FriendRequest;

/**
 * 
 * @author Maxi Rosson
 */
public class InMemoryFriendRequestsRepository extends SynchronizedInMemoryRepository<FriendRequest> implements
		FriendRequestsRepository {
	
}
