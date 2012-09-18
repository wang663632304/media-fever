package com.mediafever.repository;

import com.jdroid.android.repository.SynchronizedInMemoryRepository;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class InMemoryFriendsRepository extends SynchronizedInMemoryRepository<UserImpl> implements FriendsRepository {
	
}
