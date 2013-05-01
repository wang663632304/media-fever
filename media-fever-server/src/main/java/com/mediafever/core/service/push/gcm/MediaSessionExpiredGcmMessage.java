package com.mediafever.core.service.push.gcm;

import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionExpiredGcmMessage extends DefaultGcmMessage {
	
	private static final String MESSAGE_KEY = "mediaSessionExpired";
	
	private static final String MEDIA_SESSION_ID_KEY = "mediaSessionId";
	
	public MediaSessionExpiredGcmMessage(Long mediaSessionId) {
		addParameter(MEDIA_SESSION_ID_KEY, mediaSessionId.toString());
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.DefaultGcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
