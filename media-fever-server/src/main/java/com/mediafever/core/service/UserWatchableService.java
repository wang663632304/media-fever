package com.mediafever.core.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.javaweb.guava.function.IdPropertyFunction;
import com.jdroid.javaweb.guava.function.PropertyFunction;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.User;
import com.mediafever.core.domain.UserWatchable;
import com.mediafever.core.domain.watchable.Episode;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.repository.UserRepository;
import com.mediafever.core.repository.UserWatchableRepository;
import com.mediafever.core.repository.WatchableRepository;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class UserWatchableService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WatchableRepository watchableRepository;
	
	@Autowired
	private UserWatchableRepository userWatchableRepository;
	
	public PagedResult<UserWatchable> searchUserWatchables(Filter filter) {
		return userWatchableRepository.search(filter);
	}
	
	public UserWatchable searchUserWatchable(Long userId, Long watchableId) {
		UserWatchable userWatchable = null;
		try {
			userWatchable = userWatchableRepository.get(userId, watchableId);
		} catch (ObjectNotFoundException e) {
			User user = userRepository.get(userId);
			Watchable watchable = watchableRepository.get(watchableId);
			userWatchable = new UserWatchable(user, watchable);
		}
		return userWatchable;
	}
	
	@Transactional
	public UserWatchable createUserWatchable(Long userId, Long watchableId, Boolean watched, Boolean isInWishList) {
		User user = userRepository.get(userId);
		Watchable watchable = watchableRepository.get(watchableId);
		UserWatchable userWatchable = new UserWatchable(user, watchable, watched, isInWishList);
		userWatchableRepository.add(userWatchable);
		return userWatchable;
	}
	
	@Transactional
	public void updateUserWatchable(Long userWatchableId, Boolean watched, Boolean isInWishList) {
		UserWatchable userWatchable = userWatchableRepository.get(userWatchableId);
		userWatchable.modify(watched, isInWishList);
	}
	
	@Transactional
	public void markAsWatched(Long userId, List<Long> watchablesIds, Boolean watched) {
		List<UserWatchable> userWatchables = userWatchableRepository.findAll(userId, watchablesIds);
		List<Long> modifiedWatchablesIds = Lists.newArrayList();
		for (UserWatchable userWatchable : userWatchables) {
			userWatchable.modify(watched, userWatchable.isInWishList());
			modifiedWatchablesIds.add(userWatchable.getWatchable().getId());
		}
		for (Long watchableId : watchablesIds) {
			if (!modifiedWatchablesIds.contains(watchableId)) {
				User user = userRepository.get(userId);
				Watchable watchable = watchableRepository.get(watchableId);
				UserWatchable userWatchable = new UserWatchable(user, watchable, watched, false);
				userWatchableRepository.add(userWatchable);
			}
		}
	}
	
	public List<UserWatchable> getEpisodeUserWatchables(Long userId, List<Long> episodeIds) {
		
		User user = userRepository.get(userId);
		List<UserWatchable> userWatchables = userWatchableRepository.findAll(userId, episodeIds);
		List<Long> existentWatchablesIds = Lists.transform(userWatchables, new Function<UserWatchable, Long>() {
			
			@Override
			public Long apply(UserWatchable from) {
				return from.getWatchable().getId();
			}
		});
		
		for (Long watchableId : episodeIds) {
			if (!existentWatchablesIds.contains(watchableId)) {
				Watchable watchable = watchableRepository.get(watchableId);
				userWatchables.add(new UserWatchable(user, watchable));
			}
		}
		
		Collections.sort(userWatchables, new Comparator<UserWatchable>() {
			
			@Override
			public int compare(UserWatchable userWatchable1, UserWatchable userWatchable2) {
				Episode episode1 = (Episode)userWatchable1.getWatchable();
				Episode episode2 = (Episode)userWatchable2.getWatchable();
				return episode1.getEpisodeNumber().compareTo(episode2.getEpisodeNumber());
			};
		});
		return userWatchables;
	}
	
	public List<User> getWatchedBy(List<User> users, Watchable watchable) {
		List<User> watchedByUsers = Lists.newArrayList();
		if (!users.isEmpty()) {
			List<Long> userIds = Lists.transform(users, new IdPropertyFunction());
			watchedByUsers = Lists.transform(userWatchableRepository.getWatchedBy(userIds, watchable.getId()),
				new PropertyFunction<UserWatchable, User>("user"));
		}
		return watchedByUsers;
	}
	
	public List<User> getOnTheWishListOf(List<User> users, Watchable watchable) {
		List<User> onTheWishListOfUsers = Lists.newArrayList();
		if (!users.isEmpty()) {
			List<Long> userIds = Lists.transform(users, new IdPropertyFunction());
			onTheWishListOfUsers = Lists.transform(
				userWatchableRepository.getOnTheWishListOf(userIds, watchable.getId()),
				new PropertyFunction<UserWatchable, User>("user"));
		}
		return onTheWishListOfUsers;
	}
}
