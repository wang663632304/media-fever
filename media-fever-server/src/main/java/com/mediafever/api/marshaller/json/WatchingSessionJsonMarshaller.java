package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.watchingsession.WatchingSession;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionJsonMarshaller implements Marshaller<WatchingSession, JsonMap> {
	
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String USERS = "users";
	private static final String SELECTIONS = "selections";
	private static final String WATCHABLES_TYPES = "watchablesTypes";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(WatchingSession watchingSession, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, watchingSession.getId());
		map.put(DATE, watchingSession.getDate());
		map.put(USERS, watchingSession.getUsers());
		if (MarshallerMode.COMPLETE.equals(mode)) {
			map.put(SELECTIONS, watchingSession.getSelections());
		}
		map.put(WATCHABLES_TYPES, watchingSession.getWatchableTypes());
		return map;
	}
}
