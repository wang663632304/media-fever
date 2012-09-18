package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.watchable.Episode;

/**
 * 
 * @author Maxi Rosson
 */
public class EpisodeJsonMarshaller extends WatchableJsonMarshaller<Episode> {
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	
	/**
	 * @see com.mediafever.api.marshaller.json.WatchableJsonMarshaller#marshall(com.mediafever.core.domain.watchable.Watchable,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(Episode episode, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = super.marshall(episode, mode, extras);
		map.put(ID, episode.getId());
		map.put(NAME, episode.getName());
		map.put(NUMBER, episode.getEpisodeNumber());
		return map;
	}
}
