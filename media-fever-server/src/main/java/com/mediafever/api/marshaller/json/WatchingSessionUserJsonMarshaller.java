package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.watchingsession.WatchingSessionUser;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionUserJsonMarshaller implements Marshaller<WatchingSessionUser, JsonMap> {
	
	private static final String USER = "user";
	private static final String PENDING_THUMBS_UP = "pendingThumbsUp";
	private static final String PENDING_THUMBS_DOWN = "pendingThumbsDown";
	private static final String ACCEPTED = "accepted";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(WatchingSessionUser watchingSessionUser, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(USER, watchingSessionUser.getUser());
		map.put(PENDING_THUMBS_UP, watchingSessionUser.getPendingThumbsUp());
		map.put(PENDING_THUMBS_DOWN, watchingSessionUser.getPendingThumbsDown());
		map.put(ACCEPTED, watchingSessionUser.isAccepted());
		return map;
	}
}
