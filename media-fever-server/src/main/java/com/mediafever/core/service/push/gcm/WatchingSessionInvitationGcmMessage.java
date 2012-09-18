package com.mediafever.core.service.push.gcm;

import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionInvitationGcmMessage extends DefaultGcmMessage {
	
	private static final String MESSAGE_KEY = "watchingSessionInvitation";
	
	private static final String FRIEND_FULL_NAME_KEY = "friendFullName";
	private static final String FRIEND_IMAGE_URL_KEY = "friendImageUrl";
	
	public WatchingSessionInvitationGcmMessage(String friendFullName, String friendImageUrl) {
		addParameter(FRIEND_FULL_NAME_KEY, friendFullName);
		addParameter(FRIEND_IMAGE_URL_KEY, friendImageUrl);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.DefaultGcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
