package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.google.common.collect.Lists;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.javaweb.guava.function.IdPropertyFunction;
import com.mediafever.core.domain.session.MediaSelection;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionJsonMarshaller implements Marshaller<MediaSelection, JsonMap> {
	
	private static final String ID = "id";
	private static final String WATCHABLE = "watchable";
	private static final String THUMBS_UP_USERS = "thumbsUpUsers";
	private static final String THUMBS_DOWN_USERS = "thumbsDownUsers";
	private static final String OWNER_ID = "ownerId";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(MediaSelection mediaSelection, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, mediaSelection.getId());
		map.put(OWNER_ID, mediaSelection.getOwner().getId());
		map.put(WATCHABLE, mediaSelection.getWatchable(), MarshallerMode.SIMPLE);
		map.put(THUMBS_UP_USERS, Lists.transform(mediaSelection.getThumbsUpUsers(), new IdPropertyFunction()));
		map.put(THUMBS_DOWN_USERS, Lists.transform(mediaSelection.getThumbsDownUsers(), new IdPropertyFunction()));
		return map;
	}
}
