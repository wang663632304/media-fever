package com.mediafever.core.service.push.gcm;

import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionLeftGcmMessage extends DefaultGcmMessage {
	
	private static final String MESSAGE_KEY = "mediaSessionLeft";
	
	private static final String MEDIA_SESSION_ID_KEY = "mediaSessionId";
	private static final String FULL_NAME_KEY = "fullName";
	private static final String IMAGE_URL_KEY = "imageUrl";
	
	public MediaSessionLeftGcmMessage(Long mediaSessionId, String fullName, String imageUrl) {
		addParameter(MEDIA_SESSION_ID_KEY, mediaSessionId.toString());
		addParameter(FULL_NAME_KEY, fullName);
		addParameter(IMAGE_URL_KEY, imageUrl);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.DefaultGcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
