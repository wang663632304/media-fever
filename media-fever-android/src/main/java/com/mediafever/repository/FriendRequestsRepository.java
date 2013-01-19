package com.mediafever.repository;

import com.jdroid.android.repository.SynchronizedRepository;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public interface FriendRequestsRepository extends SynchronizedRepository<FriendRequest> {
	
	public FriendRequest getBySender(UserImpl sender);
	
}
