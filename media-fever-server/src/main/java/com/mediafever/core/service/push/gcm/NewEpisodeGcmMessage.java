package com.mediafever.core.service.push.gcm;

import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
public class NewEpisodeGcmMessage extends DefaultGcmMessage {
	
	private static final String MESSAGE_KEY = "newEpisode";
	
	private static final String SERIES_ID_KEY = "seriesId";
	private static final String SERIES_NAME_KEY = "seriesName";
	private static final String EPISODE_NAME_KEY = "episodeName";
	private static final String EPISODE_NUMBER_KEY = "episodeNumber";
	private static final String EPISODE_IMAGE_URL_KEY = "episodeImageUrl";
	
	public NewEpisodeGcmMessage(Long seriesId, String seriesName, String episodeName, Integer episodeNumber,
			String episodeImageUrl) {
		addParameter(SERIES_ID_KEY, seriesId.toString());
		addParameter(SERIES_NAME_KEY, seriesName);
		addParameter(EPISODE_NAME_KEY, episodeName);
		addParameter(EPISODE_NUMBER_KEY, episodeNumber != null ? episodeNumber.toString() : null);
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
