package com.mediafever.service;

import java.util.Date;
import java.util.List;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.search.PagedResult;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.SocialUser;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.social.FacebookAccount;
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
	
	public void logout(Long userId);
	
	/**
	 * Enable the device to start receiving GCM messages
	 * 
	 * @param registrationId The registration id
	 */
	public void enableDevice(String registrationId);
	
	public List<Watchable> searchSuggestedWatchables(String query, List<WatchableType> watchableTypes);
	
	public PagedResult<Watchable> searchWatchables(String query, List<WatchableType> watchableTypes);
	
	public UserWatchable<Watchable> getUserWatchable(Long userId, Long watchableId);
	
	public UserImpl signup(UserImpl user);
	
	public List<UserImpl> getFriends(Long userId);
	
	public List<SocialUser> getFacebookFriends(Long userId);
	
	public List<UserImpl> searchUsers(Long userId, String query);
	
	public void removeFriend(Long userId, Long friendId);
	
	public PagedResult<Watchable> getLatestWatchables();
	
	public PagedResult<Watchable> searchWatched(Long userId, List<WatchableType> watchableTypes);
	
	public PagedResult<Watchable> searchWishList(Long userId, List<WatchableType> watchableTypes);
	
	public PagedResult<Watchable> searchSuggestions(Long userId, List<WatchableType> watchableTypes);
	
	public UserWatchable<Watchable> createUserWatchable(UserWatchable<Watchable> userWatchable);
	
	public void updateUserWatchable(UserWatchable<Watchable> userWatchable);
	
	public List<FriendRequest> getFriendRequests(Long userId);
	
	public FriendRequest createFriendRequest(Long userId, Long senderId);
	
	public void acceptFriendRequest(FriendRequest friendRequest);
	
	public void rejectFriendRequest(FriendRequest friendRequest);
	
	public UserImpl editUser(Long userId, UserImpl user, FileContent avatar);
	
	/**
	 * Links an user account to his Facebook account.
	 * 
	 * @param userId The user id.
	 * @param facebookUserId The FB user id.
	 * @param facebookAccessToken The FB access token.
	 * @param facebookExpirationDate FB session's expiration date.
	 */
	public void connectToFacebook(Long userId, String facebookUserId, String facebookAccessToken,
			Date facebookExpirationDate);
	
	/**
	 * Disconnects an user account from its Facebook profile.
	 * 
	 * @param userId The user id.
	 */
	public void disconnectFromFacebook(Long userId);
	
	/**
	 * Gets the user's {@link FacebookAccount} if any is linked.
	 * 
	 * @param userId The user id.
	 * @return The {@link FacebookAccount}.
	 */
	public FacebookAccount getFacebookAccount(Long userId);
	
	public void markAsWatched(Long userId, List<Long> watchablesIds, Boolean watched);
	
	public List<MediaSession> getMediaSessions(Long userId);
	
	public void acceptMediaSession(MediaSession mediaSession);
	
	public void rejectMediaSession(MediaSession mediaSession);
	
	public MediaSession createMediaSession(MediaSession mediaSession);
	
	public void updateMediaSession(MediaSession mediaSession);
	
	public void thumbsUpMediaSelection(MediaSession mediaSession, MediaSelection mediaSelection);
	
	public void thumbsDownMediaSelection(MediaSession mediaSession, MediaSelection mediaSelection);
	
	public void removeMediaSelection(MediaSession mediaSession, MediaSelection mediaSelection);
	
	public MediaSelection addSmartSelection(MediaSession mediaSession);
	
	public MediaSelection addRandomSelection(MediaSession mediaSession);
	
	public MediaSelection addManualSelection(MediaSession mediaSession, Watchable watchable);
	
	public MediaSession getMediaSession(Long mediaSessionId);
	
	public void leaveMediaSession(Long mediaSessionId);
	
}
