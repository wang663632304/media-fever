package com.mediafever.core.repository;

import java.util.List;
import com.jdroid.java.repository.Repository;
import com.mediafever.core.domain.FriendRequest;
import com.mediafever.core.domain.User;

/**
 * Repository that handles the persistence of {@link FriendRequest}s.
 * 
 * @author Maxi Rosson
 */
public interface FriendRequestRepository extends Repository<FriendRequest> {
	
	/**
	 * @param userId The id of the {@link User}
	 * @return the {@link FriendRequest}s
	 */
	public List<FriendRequest> getByUserId(Long userId);
	
}
