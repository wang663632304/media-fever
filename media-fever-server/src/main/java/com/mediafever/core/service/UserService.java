package com.mediafever.core.service;

import java.util.Date;
import java.util.List;
import javax.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.java.collections.Lists;
import com.jdroid.javaweb.domain.FileEntity;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceRepository;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushService;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.api.exception.ServerErrorCode;
import com.mediafever.core.domain.FacebookAccount;
import com.mediafever.core.domain.FacebookUser;
import com.mediafever.core.domain.User;
import com.mediafever.core.repository.CustomFilterKey;
import com.mediafever.core.repository.UserRepository;
import com.mediafever.core.service.push.gcm.FriendRemovedGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	@Autowired
	private PushService pushService;
	
	/**
	 * Adds a user to the repository.
	 * 
	 * @param email The {@link User} email
	 * @param password The {@link User} password
	 * @param firstName The {@link User} firstName
	 * @param lastName The {@link User} lastName
	 * @param publicProfile The {@link User} publicProfile flag
	 * @param installationId
	 * @param deviceType
	 * 
	 * @return The added {@link User}
	 */
	@Transactional
	public User addUser(String email, String password, String firstName, String lastName, Boolean publicProfile,
			String installationId, DeviceType deviceType) {
		if (userRepository.existsWithEmail(email)) {
			throw ServerErrorCode.USERNAME_DUPLICATED.newBusinessException(email);
		}
		User user = new User(email, password, firstName, lastName, publicProfile);
		
		Device device = deviceRepository.find(installationId, deviceType);
		if (device == null) {
			device = new Device(installationId, deviceType);
			deviceRepository.add(device);
		}
		user.addDevice(device);
		
		userRepository.add(user);
		return user;
	}
	
	@Transactional
	public User updateUser(Long userId, String email, String password, String firstName, String lastName,
			Boolean publicProfile, FileEntity image) {
		User user = userRepository.get(userId);
		if (!email.equals(user.getEmail()) && userRepository.existsWithEmail(email)) {
			throw ServerErrorCode.USERNAME_DUPLICATED.newBusinessException(email);
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
	 * Return all the Facebook's friends of the user
	 * 
	 * @param userId The user id
	 * @return The list of friends
	 */
	public List<FacebookUser> getFacebookFriends(Long userId) {
		// TODO Implement this.
		// 1. Obtain all the Facebook friends of the logged user
		// 2. For each friend, see if it match by facebookId or email with any of our app users
		// 3. For each matched user, create a FacebookUser with our user id, first name and last name
		// 4. For each not matched user, create a FacebookUser with the facebookId, first name and last name
		
		List<FacebookUser> socialUsers = Lists.newArrayList();
		socialUsers.add(new FacebookUser("facebookId1", "firstName1", "lastName1"));
		FacebookUser user2 = new FacebookUser(null, "firstName2", "lastName2");
		user2.setId(2L);
		socialUsers.add(user2);
		return socialUsers;
	}
	
	/**
	 * Invite the user to use the app. Post on the wall if possible, else send an email
	 * 
	 * @param facebookId The invited user's facebook id
	 */
	public void inviteFacebookFriend(@PathParam("id") Long facebookId) {
		// TODO Implement this
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
		
		pushService.send(new FriendRemovedGcmMessage(userId), friend.getDevices());
	}
}
