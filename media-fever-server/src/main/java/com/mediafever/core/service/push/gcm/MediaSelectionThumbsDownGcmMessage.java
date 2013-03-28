package com.mediafever.core.service.push.gcm;

import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionThumbsDownGcmMessage extends DefaultGcmMessage {
	
	private static final String MESSAGE_KEY = "mediaSelectionThumbsDown";
	
	private static final String MEDIA_SESSION_ID_KEY = "mediaSessionId";
	private static final String WATCHABLE_NAME_KEY = "watchableName";
	private static final String FULL_NAME_KEY = "fullName";
	private static final String IMAGE_URL_KEY = "imageUrl";
	
	public MediaSelectionThumbsDownGcmMessage(Long mediaSessionId, String watchableName, String friendFullName,
			String friendImageUrl) {
		addParameter(MEDIA_SESSION_ID_KEY, mediaSessionId.toString());
		addParameter(WATCHABLE_NAME_KEY, watchableName);
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
