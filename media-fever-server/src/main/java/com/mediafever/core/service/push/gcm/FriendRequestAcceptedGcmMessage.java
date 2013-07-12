package com.mediafever.core.service.push.gcm;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestAcceptedGcmMessage extends AuthenticatedGcmMessage {
	
	private static final String MESSAGE_KEY = "friendRequestAccepted";
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.DefaultGcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
