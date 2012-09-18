package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.google.common.collect.Lists;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.javaweb.guava.function.IdPropertyFunction;
import com.mediafever.core.domain.watchingsession.WatchingSelection;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSelectionJsonMarshaller implements Marshaller<WatchingSelection, JsonMap> {
	
	private static final String WATCHABLE_ID = "watchableId";
	private static final String THUMBS_UP_USERS = "thumbsUpUsers";
	private static final String THUMBS_DOWN_USERS = "thumbsDownUsers";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(WatchingSelection watchingSelection, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(WATCHABLE_ID, watchingSelection.getWatchable().getId());
		map.put(THUMBS_UP_USERS, Lists.transform(watchingSelection.getThumbsUpUsers(), new IdPropertyFunction()));
		map.put(THUMBS_DOWN_USERS, Lists.transform(watchingSelection.getThumbsDownUsers(), new IdPropertyFunction()));
		return map;
	}
}
