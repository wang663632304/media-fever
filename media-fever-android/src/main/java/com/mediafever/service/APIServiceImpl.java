package com.mediafever.service;

import java.util.Date;
import java.util.List;
import com.jdroid.android.debug.mocks.AndroidJsonMockWebService;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.search.PagedResult;
import com.jdroid.android.utils.BitmapUtils;
import com.jdroid.java.api.AbstractApiService;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.HttpClientFactory;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.MultipartWebService;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.post.EntityEnclosingWebService;
import com.jdroid.java.marshaller.MarshallerProvider;
import com.jdroid.java.parser.json.JsonArrayParser;
import com.mediafever.context.ApplicationContext;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.social.FacebookAccount;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.parser.FacebookAccountParser;
import com.mediafever.parser.FriendRequestParser;
import com.mediafever.parser.InnerWatchableParser;
import com.mediafever.parser.MediaSelectionParser;
import com.mediafever.parser.MediaSessionParser;
import com.mediafever.parser.PagedResultParser;
import com.mediafever.parser.UserParser;
import com.mediafever.parser.UserWatchableParser;
import com.mediafever.parser.WatchableParser;
import com.mediafever.service.marshaller.DeviceJsonMarshaller;
import com.mediafever.service.marshaller.FacebookAccountJsonMarshaller;
import com.mediafever.service.marshaller.FriendRequestJsonMarshaller;
import com.mediafever.service.marshaller.LoginJsonMarshaller;
import com.mediafever.service.marshaller.MarkAsWatchedJsonMarshaller;
import com.mediafever.service.marshaller.MarkAsWatchedJsonMarshaller.MarkAsWatched;
import com.mediafever.service.marshaller.MediaSessionJsonMarshaller;
import com.mediafever.service.marshaller.UserJsonMarshaller;
import com.mediafever.service.marshaller.UserWatchableJsonMarshaller;

/**
 * 
 * @author Maxi Rosson
 */
public class APIServiceImpl extends AbstractApiService implements APIService {
	
	// Common Parameters
	private static final String PAGE = "page";
	private static final Integer DEFAULT_PAGE_VALUE = 1;
	private static final String PAGE_SIZE = "pageSize";
	private static final Integer DEFAULT_PAGE_SIZE_VALUE = 200;
	private static final String QUERY = "query";
	private static final String WATCHABLE_TYPE = "watchableType";
	
	// Auth Module
	private static final String AUTH_MODULE = "auth";
	private static final String LOGIN_ACTION = "login";
	
	// User module
	private static final String USERS_MODULE = "users";
	
	private static final String WATCHABLES = "watchables";
	private static final String WATCHABLE_ID = "watchableId";
	private static final String LATEST = "latest";
	private static final String SUGGESTIONS = "suggestions";
	private static final String WATCHED = "watched";
	private static final String IS_IN_WISHLIST = "isInWishList";
	private static final String USERS = "users";
	private static final String USER_ID = "userId";
	private static final String USER_WATCHABLES = "userWatchables";
	private static final String FRIENDS = "friends";
	private static final String FRIEND_REQUESTS = "friendRequests";
	private static final String MEDIA_SESSIONS = "mediaSessions";
	private static final String THUMBS_UP = "thumbsUp";
	private static final String THUMBS_DOWN = "thumbsDown";
	private static final String ACCEPT = "accept";
	private static final String REJECT = "reject";
	private static final String DEVICES = "devices";
	private static final String SEARCH = "search";
	private static final String MARK_AS_WATCHED = "markAsWatched";
	private static final String PROFILE = "profile";
	private static final String IMAGE = "image";
	private static final String FACEBOOK = "facebook";
	
	public APIServiceImpl() {
		MarshallerProvider.get().addMarshaller(UserImpl.class, new UserJsonMarshaller());
		MarshallerProvider.get().addMarshaller(UserWatchable.class, new UserWatchableJsonMarshaller());
		MarshallerProvider.get().addMarshaller(FriendRequest.class, new FriendRequestJsonMarshaller());
		MarshallerProvider.get().addMarshaller(MarkAsWatched.class, new MarkAsWatchedJsonMarshaller());
		MarshallerProvider.get().addMarshaller(MediaSession.class, new MediaSessionJsonMarshaller());
	}
	
	/**
	 * @see com.mediafever.service.APIService#login(java.lang.String, java.lang.String)
	 */
	@Override
	public UserImpl login(String email, String password) {
		EntityEnclosingWebService webservice = newPostService(AUTH_MODULE, LOGIN_ACTION);
		webservice.setEntity(new LoginJsonMarshaller().marshall(email, password));
		return webservice.execute(new UserParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#signup(com.mediafever.domain.UserImpl)
	 */
	@Override
	public UserImpl signup(UserImpl user) {
		EntityEnclosingWebService webservice = newPostService(USERS_MODULE);
		marshall(webservice, user);
		return webservice.execute(new UserParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#editUser(java.lang.Long, com.mediafever.domain.UserImpl,
	 *      com.jdroid.android.domain.FileContent)
	 */
	@Override
	public UserImpl editUser(Long userId, UserImpl user, FileContent avatar) {
		MultipartWebService webservice = newMultipartPutService(USERS_MODULE, userId);
		webservice.addJsonPart(PROFILE, user);
		if (avatar != null) {
			webservice.addPart(IMAGE, BitmapUtils.toPNGInputStream(avatar.getUri(), 120, 120), MimeType.PNG.toString(),
				"photo.png");
		}
		return webservice.execute(new UserParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#connectToFacebook(java.lang.Long, java.lang.String, java.lang.String,
	 *      java.util.Date)
	 */
	@Override
	public void connectToFacebook(Long userId, String facebookUserId, String facebookAccessToken,
			Date facebookExpirationDate) {
		EntityEnclosingWebService webservice = newPostService(USERS_MODULE, userId, FACEBOOK);
		webservice.setEntity(new FacebookAccountJsonMarshaller().marshall(facebookUserId, facebookAccessToken,
			facebookExpirationDate));
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#disconnectFromFacebook(java.lang.Long)
	 */
	@Override
	public void disconnectFromFacebook(Long userId) {
		WebService webservice = newDeleteService(USERS_MODULE, userId, FACEBOOK);
		webservice.execute();
	}
	
	@Override
	public FacebookAccount getFacebookAccount(Long userId) {
		WebService webservice = newGetService(USERS_MODULE, userId, FACEBOOK);
		return webservice.execute(new FacebookAccountParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#getFriends(java.lang.Long)
	 */
	@Override
	public List<UserImpl> getFriends(Long userId) {
		WebService webservice = newGetService(USERS_MODULE, userId, FRIENDS);
		return webservice.execute(new JsonArrayParser(new UserParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#removeFriend(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void removeFriend(Long userId, Long friendId) {
		WebService webservice = newDeleteService(USERS_MODULE, userId, FRIENDS, friendId);
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#searchUsers(java.lang.Long, java.lang.String)
	 */
	@Override
	public List<UserImpl> searchUsers(Long userId, String query) {
		WebService webservice = newGetService(USERS_MODULE, userId);
		webservice.addQueryParameter(PAGE, DEFAULT_PAGE_VALUE);
		webservice.addQueryParameter(PAGE_SIZE, 100);
		webservice.addQueryParameter("query", query);
		PagedResult<UserImpl> resultList = webservice.execute(new PagedResultParser(new UserParser()));
		return resultList.getResults();
	}
	
	/**
	 * @see com.mediafever.service.APIService#enableDevice(java.lang.String, java.lang.String)
	 */
	@Override
	public void enableDevice(String installationId, String registrationId) {
		EntityEnclosingWebService webservice = newPostService(DEVICES);
		webservice.setEntity(new DeviceJsonMarshaller().marshall(installationId, registrationId));
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#disableDevice(java.lang.String, java.lang.String)
	 */
	@Override
	public void disableDevice(String installationId, String userToken) {
		WebService webservice = newDeleteService(DEVICES, installationId);
		if (userToken != null) {
			webservice.addHeader(HeadersAppender.USER_TOKEN_HEADER, userToken);
		}
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#searchSuggestedWatchables(java.lang.String, java.util.List)
	 */
	@Override
	public List<Watchable> searchSuggestedWatchables(String query, List<WatchableType> watchableTypes) {
		WebService webservice = newGetService(WATCHABLES);
		webservice.addQueryParameter(QUERY, query);
		webservice.addQueryParameter(WATCHABLE_TYPE, watchableTypes);
		webservice.addQueryParameter(PAGE, DEFAULT_PAGE_VALUE);
		webservice.addQueryParameter(PAGE_SIZE, 7);
		PagedResult<Watchable> resultList = webservice.execute(new PagedResultParser(new WatchableParser()));
		return resultList.getResults();
	}
	
	/**
	 * @see com.mediafever.service.APIService#searchWatchables(java.lang.String, java.util.List)
	 */
	@Override
	public PagedResult<Watchable> searchWatchables(String query, List<WatchableType> watchableTypes) {
		WebService webservice = newGetService(WATCHABLES);
		webservice.addQueryParameter(QUERY, query);
		webservice.addQueryParameter(WATCHABLE_TYPE, watchableTypes);
		addPagination(webservice);
		return webservice.execute(new PagedResultParser(new WatchableParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#searchWatched(java.lang.Long, java.util.List)
	 */
	@Override
	public PagedResult<Watchable> searchWatched(Long userId, List<WatchableType> watchableTypes) {
		WebService webservice = newGetService(USER_WATCHABLES, SEARCH);
		webservice.addQueryParameter(USER_ID, userId);
		webservice.addQueryParameter(WATCHED, true);
		webservice.addQueryParameter(WATCHABLE_TYPE, watchableTypes);
		addPagination(webservice);
		return webservice.execute(new PagedResultParser(new InnerWatchableParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#searchWishList(java.lang.Long, java.util.List)
	 */
	@Override
	public PagedResult<Watchable> searchWishList(Long userId, List<WatchableType> watchableTypes) {
		WebService webservice = newGetService(USER_WATCHABLES, SEARCH);
		webservice.addQueryParameter(USER_ID, userId);
		webservice.addQueryParameter(IS_IN_WISHLIST, true);
		webservice.addQueryParameter(WATCHABLE_TYPE, watchableTypes);
		addPagination(webservice);
		return webservice.execute(new PagedResultParser(new InnerWatchableParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#searchSuggestions(java.lang.Long, java.util.List)
	 */
	@Override
	public PagedResult<Watchable> searchSuggestions(Long userId, List<WatchableType> watchableTypes) {
		WebService webservice = newGetService(USERS, userId, WATCHABLES, SUGGESTIONS);
		webservice.addQueryParameter(WATCHABLE_TYPE, watchableTypes);
		webservice.addQueryParameter(PAGE, DEFAULT_PAGE_VALUE);
		webservice.addQueryParameter(PAGE_SIZE, 50);
		return webservice.execute(new PagedResultParser(new WatchableParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#getLatestWatchables()
	 */
	@Override
	public PagedResult<Watchable> getLatestWatchables() {
		WebService webservice = newGetService(WATCHABLES, LATEST);
		webservice.addQueryParameter(PAGE, DEFAULT_PAGE_VALUE);
		webservice.addQueryParameter(PAGE_SIZE, 10);
		return webservice.execute(new PagedResultParser(new WatchableParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#addSmartSelection(com.mediafever.domain.session.MediaSession)
	 */
	@Override
	public MediaSelection addSmartSelection(MediaSession mediaSession) {
		WebService webservice = newPutService(MEDIA_SESSIONS, mediaSession.getId(), "mediaSelection", "smart");
		return webservice.execute(new MediaSelectionParser(mediaSession.getMediaSessionUsers()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#addManualSelection(com.mediafever.domain.session.MediaSession,
	 *      com.mediafever.domain.watchable.Watchable)
	 */
	@Override
	public MediaSelection addManualSelection(MediaSession mediaSession, Watchable watchable) {
		WebService webservice = newPutService(MEDIA_SESSIONS, mediaSession.getId(), "mediaSelection", "manual",
			watchable.getId());
		return webservice.execute(new MediaSelectionParser(mediaSession.getMediaSessionUsers()));
	}
	
	private void addPagination(WebService webservice) {
		webservice.addQueryParameter(PAGE, DEFAULT_PAGE_VALUE);
		webservice.addQueryParameter(PAGE_SIZE, DEFAULT_PAGE_SIZE_VALUE);
	}
	
	/**
	 * @see com.mediafever.service.APIService#getUserWatchable(java.lang.Long, java.lang.Long)
	 */
	@Override
	public UserWatchable<Watchable> getUserWatchable(Long userId, Long watchableId) {
		WebService webservice = newGetService(USER_WATCHABLES);
		webservice.addQueryParameter(USER_ID, userId);
		webservice.addQueryParameter(WATCHABLE_ID, watchableId);
		return webservice.execute(new UserWatchableParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#createUserWatchable(com.mediafever.domain.UserWatchable)
	 */
	@Override
	public UserWatchable<Watchable> createUserWatchable(UserWatchable<Watchable> userWatchable) {
		EntityEnclosingWebService webservice = newPostService(USER_WATCHABLES);
		marshall(webservice, userWatchable);
		return webservice.execute(new UserWatchableParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#updateUserWatchable(com.mediafever.domain.UserWatchable)
	 */
	@Override
	public void updateUserWatchable(UserWatchable<Watchable> userWatchable) {
		EntityEnclosingWebService webservice = newPutService(USER_WATCHABLES, userWatchable.getId());
		marshall(webservice, userWatchable);
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#markAsWatched(java.lang.Long, java.util.List, java.lang.Boolean)
	 */
	@Override
	public void markAsWatched(Long userId, List<Long> watchablesIds, Boolean watched) {
		EntityEnclosingWebService webservice = newPutService(USER_WATCHABLES, MARK_AS_WATCHED);
		marshall(webservice, new MarkAsWatched(userId, watchablesIds, watched));
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#getFriendRequests(java.lang.Long)
	 */
	@Override
	public List<FriendRequest> getFriendRequests(Long userId) {
		WebService webservice = newGetService(FRIEND_REQUESTS);
		webservice.addQueryParameter(USER_ID, userId);
		return webservice.execute(new JsonArrayParser(new FriendRequestParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#createFriendRequest(com.mediafever.domain.FriendRequest)
	 */
	@Override
	public FriendRequest createFriendRequest(FriendRequest friendRequest) {
		EntityEnclosingWebService webservice = newPostService(FRIEND_REQUESTS);
		marshall(webservice, friendRequest);
		return webservice.execute(new FriendRequestParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#acceptFriendRequest(com.mediafever.domain.FriendRequest)
	 */
	@Override
	public void acceptFriendRequest(FriendRequest friendRequest) {
		WebService webservice = newPutService(FRIEND_REQUESTS, friendRequest.getId(), ACCEPT);
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#rejectFriendRequest(com.mediafever.domain.FriendRequest)
	 */
	@Override
	public void rejectFriendRequest(FriendRequest friendRequest) {
		WebService webservice = newPutService(FRIEND_REQUESTS, friendRequest.getId(), REJECT);
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#getMediaSessions(java.lang.Long)
	 */
	@Override
	public List<MediaSession> getMediaSessions(Long userId) {
		WebService webservice = newGetService(MEDIA_SESSIONS);
		webservice.addQueryParameter(USER_ID, userId);
		return webservice.execute(new JsonArrayParser(new MediaSessionParser()));
	}
	
	/**
	 * @see com.mediafever.service.APIService#getMediaSession(java.lang.Long)
	 */
	@Override
	public MediaSession getMediaSession(Long mediaSessionId) {
		WebService webservice = newGetService(MEDIA_SESSIONS, mediaSessionId);
		return webservice.execute(new MediaSessionParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#createMediaSession(com.mediafever.domain.session.MediaSession)
	 */
	@Override
	public MediaSession createMediaSession(MediaSession mediaSession) {
		EntityEnclosingWebService webservice = newPostService(MEDIA_SESSIONS);
		marshall(webservice, mediaSession);
		return webservice.execute(new MediaSessionParser());
	}
	
	/**
	 * @see com.mediafever.service.APIService#updateMediaSession(com.mediafever.domain.session.MediaSession)
	 */
	@Override
	public void updateMediaSession(MediaSession mediaSession) {
		EntityEnclosingWebService webservice = newPutService(MEDIA_SESSIONS, mediaSession.getId());
		marshall(webservice, mediaSession);
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#thumbsUpMediaSelection(com.mediafever.domain.session.MediaSession,
	 *      com.mediafever.domain.session.MediaSelection)
	 */
	@Override
	public void thumbsUpMediaSelection(MediaSession mediaSession, MediaSelection mediaSelection) {
		EntityEnclosingWebService webservice = newPutService(MEDIA_SESSIONS, mediaSession.getId(), THUMBS_UP,
			mediaSelection.getId());
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#thumbsDownMediaSelection(com.mediafever.domain.session.MediaSession,
	 *      com.mediafever.domain.session.MediaSelection)
	 */
	@Override
	public void thumbsDownMediaSelection(MediaSession mediaSession, MediaSelection mediaSelection) {
		EntityEnclosingWebService webservice = newPutService(MEDIA_SESSIONS, mediaSession.getId(), THUMBS_DOWN,
			mediaSelection.getId());
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#removeMediaSelection(com.mediafever.domain.session.MediaSession,
	 *      com.mediafever.domain.session.MediaSelection)
	 */
	@Override
	public void removeMediaSelection(MediaSession mediaSession, MediaSelection mediaSelection) {
		WebService webservice = newDeleteService(MEDIA_SESSIONS, mediaSession.getId(), "mediaSelection",
			mediaSelection.getId());
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#acceptMediaSession(com.mediafever.domain.session.MediaSession)
	 */
	@Override
	public void acceptMediaSession(MediaSession mediaSession) {
		WebService webservice = newPutService(MEDIA_SESSIONS, mediaSession.getId(), ACCEPT);
		webservice.execute();
	}
	
	/**
	 * @see com.mediafever.service.APIService#rejectMediaSession(com.mediafever.domain.session.MediaSession)
	 */
	@Override
	public void rejectMediaSession(MediaSession mediaSession) {
		WebService webservice = newPutService(MEDIA_SESSIONS, mediaSession.getId(), REJECT);
		webservice.execute();
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getHttpWebServiceProcessors()
	 */
	@Override
	protected List<HttpWebServiceProcessor> getHttpWebServiceProcessors() {
		return Lists.newArrayList(HeadersAppender.get(), HttpResponseValidator.get());
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getAbstractMockWebServiceInstance(java.lang.Object[])
	 */
	@Override
	protected AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments) {
		return new AndroidJsonMockWebService(urlSegments);
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#isHttpMockEnabled()
	 */
	@Override
	protected Boolean isHttpMockEnabled() {
		return ApplicationContext.get().isHttpMockEnabled();
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getServerURL()
	 */
	@Override
	protected String getServerURL() {
		return ApplicationContext.get().getServerApiUrl();
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getHttpClientFactoryInstance()
	 */
	@Override
	protected HttpClientFactory getHttpClientFactoryInstance() {
		return super.getHttpClientFactoryInstance();
	}
}