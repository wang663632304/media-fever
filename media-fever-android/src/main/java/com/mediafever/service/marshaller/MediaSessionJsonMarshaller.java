package com.mediafever.service.marshaller;

import java.util.List;
import java.util.Map;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.session.MediaSessionUser;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionJsonMarshaller implements Marshaller<MediaSession, JsonMap> {
	
	private static final String DATE = "date";
	private static final String TIME = "time";
	private static final String WATCHABLE_TYPE = "watchableTypes";
	private static final String USER_IDS = "usersIds";
	private static final String WATCHABLES_IDS = "watchablesIds";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(MediaSession mediaSession, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(DATE, mediaSession.getDate());
		map.put(TIME, mediaSession.getTime());
		map.put(WATCHABLE_TYPE, mediaSession.getWatchableTypes());
		List<Long> ids = Lists.newArrayList();
		for (MediaSessionUser user : mediaSession.getMediaSessionUsers()) {
			ids.add(user.getUser().getId());
		}
		map.put(USER_IDS, ids);
		ids = Lists.newArrayList();
		for (MediaSelection mediaSelection : mediaSession.getSelections()) {
			if (mediaSelection.getWatchable() != null) {
				ids.add(mediaSelection.getWatchable().getId());
			}
		}
		map.put(WATCHABLES_IDS, ids);
		return map;
	}
}
