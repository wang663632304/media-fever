package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionJsonMarshaller implements Marshaller<MediaSession, JsonMap> {
	
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String TIME = "time";
	private static final String USERS = "users";
	private static final String SELECTIONS = "selections";
	private static final String WATCHABLES_TYPES = "watchablesTypes";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(MediaSession mediaSession, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, mediaSession.getId());
		map.put(DATE, mediaSession.getDate());
		map.put(TIME, mediaSession.getTime());
		map.put(USERS, mediaSession.getUsers());
		if (MarshallerMode.COMPLETE.equals(mode)) {
			map.put(SELECTIONS, mediaSession.getSelections());
		}
		map.put(WATCHABLES_TYPES, mediaSession.getWatchableTypes());
		return map;
	}
}
