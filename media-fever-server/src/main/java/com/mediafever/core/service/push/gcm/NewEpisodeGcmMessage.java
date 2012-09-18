package com.mediafever.core.service.push.gcm;

import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
public class NewEpisodeGcmMessage extends DefaultGcmMessage {
	
	private static final String MESSAGE_KEY = "newEpisode";
	
	private static final String SERIES_ID_KEY = "seriesId";
	private static final String EPISODE_NAME_KEY = "episodeName";
	private static final String EPISODE_IMAGE_URL_KEY = "episodeImageUrl";
	
	public NewEpisodeGcmMessage(Long seriesId, String episodeName, String episodeImageUrl) {
		addParameter(SERIES_ID_KEY, seriesId.toString());
		addParameter(EPISODE_NAME_KEY, episodeName);
		addParameter(EPISODE_IMAGE_URL_KEY, episodeImageUrl);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.DefaultGcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
