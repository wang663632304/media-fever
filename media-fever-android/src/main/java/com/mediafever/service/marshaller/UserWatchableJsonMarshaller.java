package com.mediafever.service.marshaller;

import java.util.Map;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.parser.UserWatchableParser;

/**
 * 
 * @author Maxi Rosson
 */
public class UserWatchableJsonMarshaller implements Marshaller<UserWatchable<Watchable>, JsonMap> {
	
	private static final String USER_ID = "userId";
	private static final String WATCHABLE_ID = "watchableId";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(UserWatchable<Watchable> userWatchable, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		if (userWatchable.getId() == null) {
			map.put(UserWatchableJsonMarshaller.USER_ID, SecurityContext.get().getUser().getId());
			map.put(UserWatchableJsonMarshaller.WATCHABLE_ID, userWatchable.getWatchable().getId());
		}
		map.put(UserWatchableParser.WATCHED, userWatchable.isWatched());
		map.put(UserWatchableParser.IS_IN_WISH_LIST, userWatchable.isInWishList());
		return map;
	}
}
