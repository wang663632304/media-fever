package com.mediafever.android.gcm;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.contextual.ContextualActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.android.utils.NotificationUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.NumberUtils;
import com.mediafever.R;
import com.mediafever.android.AndroidApplication;
import com.mediafever.android.ui.friends.FriendsActivity;
import com.mediafever.android.ui.friends.FriendsContextualItem;
import com.mediafever.android.ui.friends.FriendsRequestsActivity;
import com.mediafever.android.ui.session.WatchingSessionListActivity;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.repository.FriendRequestsRepository;

/**
 * GCM Message types
 * 
 * @author Maxi Rosson
 */
public enum GcmMessage {
	
	WATCHING_SESSION_INVITATION("watchingSessionInvitation") {
		
		@Override
		public void handle(Intent intent) {
			String friendFullName = intent.getStringExtra(FRIEND_FULL_NAME_KEY);
			String friendImageUrl = intent.getStringExtra(FRIEND_IMAGE_URL_KEY);
			String tickerText = LocalizationUtils.getString(R.string.watchingSessionInvitationTickerText,
				friendFullName);
			String contentTitle = LocalizationUtils.getString(R.string.watchingSessionInvitationContentTitle);
			String contentText = LocalizationUtils.getString(R.string.watchingSessionInvitationContentText,
				friendFullName);
			
			NotificationUtils.sendNotification(IdGenerator.getIntId(), R.drawable.ic_launcher, friendImageUrl,
				tickerText, contentTitle, contentText, WatchingSessionListActivity.class);
		}
	},
	FRIEND_REQUEST("friendRequest") {
		
		@Override
		public void handle(Intent intent) {
			String friendFullName = intent.getStringExtra(FRIEND_FULL_NAME_KEY);
			String friendImageUrl = intent.getStringExtra(FRIEND_IMAGE_URL_KEY);
			String tickerText = LocalizationUtils.getString(R.string.friendRequestTickerText, friendFullName);
			String contentTitle = LocalizationUtils.getString(R.string.friendRequestContentTitle);
			String contentText = LocalizationUtils.getString(R.string.friendRequestContentText, friendFullName);
			
			Intent notificationIntent = null;
			if (AndroidUtils.isLargeScreenOrBigger()) {
				notificationIntent = new Intent(AndroidApplication.get(), FriendsActivity.class);
				notificationIntent.putExtra(ContextualActivity.DEFAULT_CONTEXTUAL_ITEM_EXTRA,
					FriendsContextualItem.REQUESTS);
			} else {
				notificationIntent = new Intent(AndroidApplication.get(), FriendsRequestsActivity.class);
			}
			
			NotificationUtils.sendNotification(IdGenerator.getIntId(), R.drawable.ic_launcher, friendImageUrl,
				tickerText, contentTitle, contentText, notificationIntent);
			
			FriendRequestsRepository friendRequestsRepository = AbstractApplication.getInstance(FriendRequestsRepository.class);
			friendRequestsRepository.resetLastUpdateTimestamp();
		}
	},
	NEW_EPISODE("newEpisode") {
		
		@Override
		public void handle(Intent intent) {
			Long seriesId = NumberUtils.getLong(intent.getStringExtra(SERIES_ID_KEY));
			String episodeName = intent.getStringExtra(EPISODE_NAME_KEY);
			
			String tickerText = LocalizationUtils.getString(R.string.newEpisodeTickerText, episodeName);
			String contentTitle = LocalizationUtils.getString(R.string.newEpisodeContentTitle);
			String contentText = LocalizationUtils.getString(R.string.newEpisodeContentText, episodeName);
			String episodeImageUrl = intent.getStringExtra(EPISODE_IMAGE_URL_KEY);
			
			Intent notificationIntent = new Intent(AndroidApplication.get(), WatchableActivity.class);
			notificationIntent.setData(Uri.parse(seriesId.toString()));
			notificationIntent.putExtra(WatchableActivity.WATCHABLE_TYPE_EXTRA, WatchableType.SERIES);
			
			NotificationUtils.sendNotification(IdGenerator.getIntId(), R.drawable.ic_launcher, episodeImageUrl,
				tickerText, contentTitle, contentText, notificationIntent);
		}
	};
	
	private static final String TAG = GcmMessage.class.getSimpleName();
	private static final String MESSAGE_KEY_EXTRA = "messageKey";
	private static final String FRIEND_FULL_NAME_KEY = "friendFullName";
	private static final String FRIEND_IMAGE_URL_KEY = "friendImageUrl";
	private static final String SERIES_ID_KEY = "seriesId";
	private static final String EPISODE_NAME_KEY = "episodeName";
	private static final String EPISODE_IMAGE_URL_KEY = "episodeImageUrl";
	private String messageKey;
	
	private GcmMessage(String messageKey) {
		this.messageKey = messageKey;
	}
	
	public static GcmMessage find(Intent intent) {
		String messageKey = intent.getStringExtra(MESSAGE_KEY_EXTRA);
		Log.d(TAG, "GCM message received. / Message Key: " + messageKey);
		for (GcmMessage each : values()) {
			if (each.messageKey.equalsIgnoreCase(messageKey)) {
				return each;
			}
		}
		Log.w(TAG, "The GCM message is unknown");
		return null;
	}
	
	public abstract void handle(Intent intent);
	
}
