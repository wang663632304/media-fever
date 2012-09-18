package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.UserWatchable;

/**
 * 
 * @author Maxi Rosson
 */
public class UserWatchableJsonMarshaller implements Marshaller<UserWatchable, JsonMap> {
	
	private static final String ID = "id";
	public static final String WATCHED = "watched";
	public static final String IS_IN_WISH_LIST = "isInWishList";
	private static final String WATCHABLE = "watchable";
	
	/**
	 * 
	 * <pre>
	 * 		{
	 * 			"id": 1,
	 * 			"watched": true,
	 *          "isInWishList": false,
	 * 			"watchable":  {
	 * 			   "id": 1,
	 * 			   "type": "Movie",
	 * 			   "name": "Sin City",
	 * 			   "image": "http://server.com/image1.png",
	 *             "releaseYear": 1984,
	 * 			   "overview": "Sin City is a neo-noir crime thriller based on ...",
	 * 			   "trailer": "http://www.youtube.com/watch?v=YKFLrTYKIXk",
	 *             "genres": [
	 *                  "Action",
	 *                  "Adventure"
	 *              ],
	 *             "actors": [
	 *                  "Jennifer Aniston",
	 *                  "Lisa Kudrow"
	 *              ]
	 * 		   }
	 * 
	 * 		}
	 * </pre>
	 * 
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(UserWatchable userWatchable, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, userWatchable.getId());
		map.put(WATCHED, userWatchable.isWatched());
		map.put(IS_IN_WISH_LIST, userWatchable.isInWishList());
		map.put(WATCHABLE, userWatchable.getWatchable());
		return map;
	}
}
