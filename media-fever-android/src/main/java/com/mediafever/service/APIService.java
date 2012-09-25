package com.mediafever.service;

import java.util.List;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.search.PagedResult;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public interface APIService {
	
	/**
	 * Login API Call
	 * 
	 * @param email
	 * @param password
	 * @return The logged {@link UserImpl} or null if the login failed
	 */
	public UserImpl login(String email, String password);
	
	/**
	 * Enable the device to start receiving GCM messages
	 * 
	 * @param installationId An unique device identifier
	 * @param registrationId The registration id
	 */
	public void enableDevice(String installationId, String registrationId);
	
	/**
	 * Disable the device to stop receiving GCM messages
	 * 
	 * @param installationId An unique device identifier
	 * @param userToken The user token
	 */
	public void disableDevice(String installationId, String userToken);
	
	public List<Watchable> searchSuggestedWatchables(String query, List<WatchableType> watchableTypes);
	
	public PagedResult<Watchable> searchWatchables(String query, List<WatchableType> watchableTypes);
	
	public UserWatchable<Watchable> getUserWatchable(Long userId, Long watchableId);
	
	public UserImpl signup(UserImpl user);
	
	public List<UserImpl> getFriends(Long userId);
	
	public void removeFriend(Long userId, Long friendId);
	
	public PagedResult<Watchable> getLatestWatchables();
	
	public PagedResult<Watchable> searchWatched(Long userId, List<WatchableType> watchableTypes);
	
	public PagedResult<Watchable> searchWishList(Long userId, List<WatchableType> watchableTypes);
	
	public PagedResult<Watchable> searchSuggestions(Long userId, List<WatchableType> watchableTypes);
	
	public UserWatchable<Watchable> createUserWatchable(UserWatchable<Watchable> userWatchable);
	
	public void updateUserWatchable(UserWatchable<Watchable> userWatchable);
	
	public List<FriendRequest> getFriendRequests(Long userId);
	
	public FriendRequest createFriendRequest(FriendRequest friendRequest);
	
	public void acceptFriendRequest(FriendRequest friendRequest);
	
	public void rejectFriendRequest(FriendRequest friendRequest);
	
	public UserImpl editUser(Long userId, UserImpl user, FileContent avatar);
	
	/**
	 * Links an user account to his facebook account.
	 * 
	 * @param userId The user id.
	 * @param facebookUserId The FB user id.
	 * @param facebookAccessToken The FB access token.
	 */
	public void connectToFacebook(Long userId, String facebookUserId, String facebookAccessToken);
	
	public void markAsWatched(Long userId, List<Long> watchablesIds, Boolean watched);
	
	public List<MediaSession> getMediaSessions(Long userId);
	
}
