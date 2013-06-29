package com.mediafever.core.service.push.gcm;


/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestGcmMessage extends AuthenticatedGcmMessage {
	
	private static final String MESSAGE_KEY = "friendRequest";
	
	private static final String FULL_NAME_KEY = "fullName";
	private static final String IMAGE_URL_KEY = "imageUrl";
	
	public FriendRequestGcmMessage(String friendFullName, String friendImageUrl) {
		addParameter(FULL_NAME_KEY, friendFullName);
		addParameter(IMAGE_URL_KEY, friendImageUrl);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.DefaultGcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
