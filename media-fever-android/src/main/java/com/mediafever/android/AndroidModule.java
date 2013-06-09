package com.mediafever.android;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.jdroid.android.repository.UserRepository;
import com.mediafever.android.repository.UserRepositoryImpl;
import com.mediafever.repository.FriendRequestsRepository;
import com.mediafever.repository.FriendsRepository;
import com.mediafever.repository.InMemoryFriendRequestsRepository;
import com.mediafever.repository.InMemoryFriendsRepository;
import com.mediafever.repository.InMemoryMediaSessionsRepository;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;
import com.mediafever.service.APIServiceImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidModule extends AbstractModule {
	
	/**
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure() {
		
		this.bind(MediaSessionsRepository.class).to(InMemoryMediaSessionsRepository.class).in(Singleton.class);
		this.bind(FriendsRepository.class).to(InMemoryFriendsRepository.class).in(Singleton.class);
		this.bind(FriendRequestsRepository.class).to(InMemoryFriendRequestsRepository.class).in(Singleton.class);
		this.bind(UserRepository.class).to(UserRepositoryImpl.class).in(Singleton.class);
		this.bind(APIService.class).to(APIServiceImpl.class).in(Singleton.class);
	}
	
}
