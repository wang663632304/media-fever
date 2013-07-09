package com.mediafever.android.gcm;

import android.content.Intent;
import android.net.Uri;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.contextual.ContextualActivity;
import com.jdroid.android.gcm.GcmMessage;
import com.jdroid.android.gcm.GcmMessageBroadcastReceiver;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.NotificationBuilder;
import com.jdroid.android.utils.NotificationUtils;
import com.jdroid.java.utils.IdGenerator;
import com.mediafever.R;
import com.mediafever.android.AndroidApplication;
import com.mediafever.android.ui.friends.FriendsActivity;
import com.mediafever.android.ui.friends.FriendsContextualItem;
import com.mediafever.android.ui.friends.FriendsRequestsActivity;
import com.mediafever.android.ui.session.MediaSessionListActivity;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.repository.FriendRequestsRepository;
import com.mediafever.repository.FriendsRepository;
import com.mediafever.repository.MediaSessionsRepository;

/**
 * GCM Message types
 * 
 * @author Maxi Rosson
 */
public enum MediaFeverGcmMessage implements GcmMessage {
	
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
			
			synchronizeFriendRequests(intent);
		}
	},
	FRIEND_REQUEST_ACCEPTED("friendRequestAccepted") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeFriendRequests(intent);
		}
	},
	FRIEND_REMOVED("friendRemoved") {
		
		@Override
		public void handle(Intent intent) {
			synchronizeFriends(intent);
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
	
	private MediaFeverGcmMessage(String messageKey) {
		this.messageKey = messageKey;
	}
	
	public void synchronizeMediaSession(Intent intent) {
		MediaSessionsRepository mediaSessionsRepository = AbstractApplication.getInstance(MediaSessionsRepository.class);
		mediaSessionsRepository.resetLastUpdateTimestamp();
		
		GcmMessageBroadcastReceiver.sendBroadcast(this, intent);
	}
	
	public void synchronizeFriendRequests(Intent intent) {
		FriendRequestsRepository friendRequestsRepository = AbstractApplication.getInstance(FriendRequestsRepository.class);
		friendRequestsRepository.resetLastUpdateTimestamp();
		
		GcmMessageBroadcastReceiver.sendBroadcast(this, intent);
	}
	
	public void synchronizeFriends(Intent intent) {
		FriendsRepository friendsRepository = AbstractApplication.getInstance(FriendsRepository.class);
		friendsRepository.resetLastUpdateTimestamp();
		
		GcmMessageBroadcastReceiver.sendBroadcast(this, intent);
	}
	
	/**
	 * @see com.jdroid.android.gcm.GcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return messageKey;
	}
	
}
