package com.mediafever.core.service.push.gcm;


/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionUpdatedGcmMessage extends AuthenticatedGcmMessage {
	
	private static final String MESSAGE_KEY = "mediaSessionUpdated";
	
	private static final String MEDIA_SESSION_ID_KEY = "mediaSessionId";
	
	public MediaSessionUpdatedGcmMessage(Long mediaSessionId) {
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
