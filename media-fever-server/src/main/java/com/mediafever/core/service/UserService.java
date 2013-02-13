package com.mediafever.core.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.java.collections.Lists;
import com.jdroid.javaweb.domain.FileEntity;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.api.exception.ServerErrorCode;
import com.mediafever.core.domain.FacebookAccount;
import com.mediafever.core.domain.User;
import com.mediafever.core.repository.CustomFilterKey;
import com.mediafever.core.repository.UserRepository;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Adds a user to the repository.
	 * 
	 * @param email The {@link User} email
	 * @param password The {@link User} password
	 * @param firstName The {@link User} firstName
	 * @param lastName The {@link User} lastName
	 * @param publicProfile The {@link User} publicProfile flag
	 * 
	 * @return The added {@link User}
	 */
	@Transactional
	public User addUser(String email, String password, String firstName, String lastName, Boolean publicProfile) {
		if (userRepository.existsWithEmail(email)) {
			throw ServerErrorCode.DUPLICATED_USERNAME.newBusinessException(email);
		}
		User user = new User(email, password, firstName, lastName, publicProfile);
		userRepository.add(user);
		return user;
	}
	
	@Transactional
	public User updateUser(Long userId, String email, String password, String firstName, String lastName,
			Boolean publicProfile, FileEntity image) {
		User user = userRepository.get(userId);
		if (!email.equals(user.getEmail()) && userRepository.existsWithEmail(email)) {
			throw ServerErrorCode.DUPLICATED_USERNAME.newBusinessException(email);
		}
		user.modify(email, password, firstName, lastName, publicProfile, image);
		return user;
	}
	
	/**
	 * Links a {@link User} to his {@link FacebookAccount}.
	 * 
	 * @param userId The user id.
	 * @param facebookUserId The FB user id.
	 * @param facebookAccessToken The FB access token.
	 * @param facebookAccessExpirationDate The FB session's expiration date.
	 */
	@Transactional
	public void linkToFacebookAccount(Long userId, String facebookUserId, String facebookAccessToken,
			Date facebookAccessExpirationDate) {
		User user = userRepository.get(userId);
		user.linkToFacebookAccount(facebookUserId, facebookAccessToken, facebookAccessExpirationDate);
	}
	
	/**
	 * Unlinks a {@link User} from his {@link FacebookAccount}.
	 * 
	 * @param userId The user id.
	 */
	@Transactional
	public void unlinkFacebookAccount(Long userId) {
		User user = userRepository.get(userId);
		user.unlinkFacebookAccount();
	}
	
	/**
	 * Returns the {@link User}'s {@link FacebookAccount}.
	 * 
	 * @param userId The user id.
	 * @return The {@link FacebookAccount}.
	 */
	public FacebookAccount getFacebookAccount(Long userId) {
		User user = userRepository.get(userId);
		return user.getFacebookAccount();
	}
	
	/**
	 * Return all the friends of the user
	 * 
	 * @param userId The user id
	 * @return The list of friends
	 */
	public List<User> getFriends(Long userId) {
		User user = userRepository.get(userId);
		return user.getFriends();
	}
	
	public PagedResult<User> search(Long userId, Filter filter) {
		filter.addValue(CustomFilterKey.USER_PUBLIC_PROFILE, true);
		PagedResult<User> result = userRepository.search(filter);
		User user = userRepository.get(userId);
		List<User> excludedUsers = Lists.newArrayList(user);
		excludedUsers.addAll(user.getFriends());
		result.getData().removeAll(excludedUsers);
		return result;
	}
	
	/**
	 * Removes the friendship between the user and the friend
	 * 
	 * @param userId The id of the user
	 * @param friendId The id of the friend to remove
	 */
	@Transactional
	public void removeFriend(Long userId, Long friendId) {
		User user = userRepository.get(userId);
		User friend = userRepository.get(friendId);
		user.removeFriend(friend);
	}
}
