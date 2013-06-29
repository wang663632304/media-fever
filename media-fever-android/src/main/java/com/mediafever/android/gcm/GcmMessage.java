package com.mediafever.android.gcm;

import org.slf4j.Logger;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.contextual.ContextualActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.NotificationBuilder;
import com.jdroid.android.utils.NotificationUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.NumberUtils;
import com.mediafever.R;
import com.mediafever.android.AndroidApplication;
import com.mediafever.android.service.LogoutService;
import com.mediafever.android.ui.friends.FriendsActivity;
import com.mediafever.android.ui.friends.FriendsContextualItem;
import com.mediafever.android.ui.friends.FriendsRequestsActivity;
import com.mediafever.android.ui.session.MediaSessionListActivity;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.repository.FriendRequestsRepository;
import com.mediafever.repository.MediaSessionsRepository;

/**
 * GCM Message types
 * 
 * @author Maxi Rosson
 */
public enum GcmMessage {
	
	MEDIA_SESSION_INVITATION("mediaSessionInvitation") {
		
		@Override
		public void handle(Intent intent) {
			String friendFullName = intent.getStringExtra(FULL_NAME_KEY);
			
			NotificationBuilder builder = new NotificationBuilder();
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setLargeIcon(intent.getStringExtra(IMAGE_URL_KEY));
			builder.setTicker(R.string.mediaSessionInvitationTickerText, friendFullName);
			builder.setContentTitle(R.string.mediaSessionInvitationContentTitle);
			builder.setContentText(R.string.mediaSessionInvitationContentText, friendFullName);
			builder.setWhen(System.currentTimeMillis());
			builder.setContentIntent(MediaSessionListActivity.class);
			
			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			
			MediaSessionsRepository mediaSessionsRepository = AbstractApplication.getInstance(MediaSessionsRepository.class);
			mediaSessionsRepository.resetLastUpdateTimestamp();
			
			synchronizeMediaSession(intent);
		}
	},
	MEDIA_SESSION_LEFT("mediaSessionLeft") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeMediaSession(intent);
		}
	},
	MEDIA_SESSION_UPDATED("mediaSessionUpdated") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeMediaSession(intent);
		}
	},
	MEDIA_SESSION_EXPIRED("mediaSessionExpired") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeMediaSession(intent);
		}
	},
	MEDIA_SELECTION_THUMBS_UP("mediaSelectionThumbsUp") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeMediaSession(intent);
		}
	},
	MEDIA_SELECTION_THUMBS_DOWN("mediaSelectionThumbsDown") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeMediaSession(intent);
		}
	},
	MEDIA_SELECTION_ADDED("mediaSelectionAdded") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeMediaSession(intent);
		}
	},
	MEDIA_SELECTION_REMOVED("mediaSelectionRemoved") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeMediaSession(intent);
		}
	},
	FRIEND_REQUEST("friendRequest") {
		
		@Override
		public void handle(Intent intent) {
			String friendFullName = intent.getStringExtra(FULL_NAME_KEY);
			
			Intent notificationIntent = null;
			if (AndroidUtils.isLargeScreenOrBigger()) {
				notificationIntent = new Intent(AndroidApplication.get(), FriendsActivity.class);
				notificationIntent.putExtra(ContextualActivity.DEFAULT_CONTEXTUAL_ITEM_EXTRA,
					FriendsContextualItem.REQUESTS);
			} else {
				notificationIntent = new Intent(AndroidApplication.get(), FriendsRequestsActivity.class);
			}
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			
			NotificationBuilder builder = new NotificationBuilder();
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setLargeIcon(intent.getStringExtra(IMAGE_URL_KEY));
			builder.setTicker(R.string.friendRequestTickerText, friendFullName);
			builder.setContentTitle(R.string.friendRequestContentTitle);
			builder.setContentText(R.string.friendRequestContentText, friendFullName);
			builder.setWhen(System.currentTimeMillis());
			builder.setContentIntent(notificationIntent);
			
			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
			
			FriendRequestsRepository friendRequestsRepository = AbstractApplication.getInstance(FriendRequestsRepository.class);
			friendRequestsRepository.resetLastUpdateTimestamp();
		}
	},
	NEW_EPISODE("newEpisode") {
		
		@Override
		public void handle(Intent intent) {
			String seriesId = intent.getStringExtra(SERIES_ID_KEY);
			String seriesName = intent.getStringExtra(SERIES_NAME_KEY);
			String episodeName = intent.getStringExtra(EPISODE_NAME_KEY);
			String episodeNumber = intent.getStringExtra(EPISODE_NUMBER_KEY);
			
			Intent notificationIntent = new Intent(AndroidApplication.get(), WatchableActivity.class);
			notificationIntent.setData(Uri.parse(seriesId));
			notificationIntent.putExtra(WatchableActivity.WATCHABLE_TYPE_EXTRA, WatchableType.SERIES);
			
			NotificationBuilder builder = new NotificationBuilder();
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setLargeIcon(intent.getStringExtra(EPISODE_IMAGE_URL_KEY));
			builder.setTicker(R.string.newEpisodeTickerText, seriesName);
			builder.setContentTitle(seriesName);
			builder.setContentText(R.string.newEpisodeContentText, episodeName != null ? episodeName : episodeNumber);
			builder.setWhen(System.currentTimeMillis());
			builder.setContentIntent(notificationIntent);
			
			NotificationUtils.sendNotification(IdGenerator.getIntId(), builder);
		}
	};
	
	private final static Logger LOGGER = LoggerUtils.getLogger(GcmMessage.class);
	
	public static final String MEDIA_SESSION_SYNCHRONIZE_ACTION = "MediaSession.SYNCHRONIZE_ACTION";
	
	private static final String REQUIRES_AUTHENTICATION_KEY_EXTRA = "requiresAuthenticationKey";
	private static final String MESSAGE_KEY_EXTRA = "messageKey";
	public static final String FULL_NAME_KEY = "fullName";
	public static final String IMAGE_URL_KEY = "imageUrl";
	private static final String SERIES_ID_KEY = "seriesId";
	private static final String SERIES_NAME_KEY = "seriesName";
	private static final String EPISODE_NAME_KEY = "episodeName";
	private static final String EPISODE_NUMBER_KEY = "episodeNumber";
	private static final String EPISODE_IMAGE_URL_KEY = "episodeImageUrl";
	public static final String MEDIA_SESSION_ID_KEY = "mediaSessionId";
	public static final String WATCHABLE_NAME_KEY = "watchableName";
	private String messageKey;
	
	private GcmMessage(String messageKey) {
		this.messageKey = messageKey;
	}
	
	public static GcmMessage find(Intent intent) {
		String messageKey = intent.getStringExtra(MESSAGE_KEY_EXTRA);
		LOGGER.debug("GCM message received. / Message Key: " + messageKey);
		for (GcmMessage each : values()) {
			if (each.messageKey.equalsIgnoreCase(messageKey)) {
				
				Boolean requiresAuthenticationKey = NumberUtils.getBoolean(
					intent.getStringExtra(REQUIRES_AUTHENTICATION_KEY_EXTRA), false);
				
				// We should ignore messages received for previously logged users
				if (requiresAuthenticationKey && !SecurityContext.get().isAuthenticated()) {
					LOGGER.warn("The GCM message is ignored because it requires to be authenticated");
					// Tell the server to remove this device to that user id
					LogoutService.runLogoutService(AndroidApplication.get());
					return null;
				}
				return each;
			}
		}
		LOGGER.warn("The GCM message key [" + messageKey + "] is unknown");
		return null;
	}
	
	private static void synchronizeMediaSession(Intent intent) {
		MediaSessionsRepository mediaSessionsRepository = AbstractApplication.getInstance(MediaSessionsRepository.class);
		mediaSessionsRepository.resetLastUpdateTimestamp();
		
		// Send the Media Session synchronize broadcast
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(MEDIA_SESSION_SYNCHRONIZE_ACTION);
		broadcastIntent.putExtras(intent.getExtras());
		LocalBroadcastManager.getInstance(AbstractApplication.get()).sendBroadcast(broadcastIntent);
	}
	
	public static void startListeningMediaSessionSynchBroadcasts(BroadcastReceiver broadcastReceiver) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MEDIA_SESSION_SYNCHRONIZE_ACTION);
		LocalBroadcastManager.getInstance(AbstractApplication.get()).registerReceiver(broadcastReceiver, intentFilter);
	}
	
	public static void stopListeningMediaSessionSynchBroadcasts(BroadcastReceiver broadcastReceiver) {
		if (broadcastReceiver != null) {
			LocalBroadcastManager.getInstance(AbstractApplication.get()).unregisterReceiver(broadcastReceiver);
		}
	}
	
	public abstract void handle(Intent intent);
	
}
