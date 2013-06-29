package com.mediafever.core.service.push.gcm;


/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionInvitationGcmMessage extends AuthenticatedGcmMessage {
	
	private static final String MESSAGE_KEY = "mediaSessionInvitation";
	
	private static final String FULL_NAME_KEY = "fullName";
	private static final String IMAGE_URL_KEY = "imageUrl";
	
	public MediaSessionInvitationGcmMessage(String friendFullName, String friendImageUrl) {
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
