package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.UserWatchable;
import com.mediafever.core.service.UserWatchableService;

/**
 * 
 * @author Maxi Rosson
 */
public class UserWatchableJsonMarshaller implements Marshaller<UserWatchable, JsonMap> {
	
	private static final String ID = "id";
	public static final String WATCHED = "watched";
	public static final String IS_IN_WISH_LIST = "isInWishList";
	private static final String WATCHABLE = "watchable";
	private static final String WATCHED_BY = "watchedBy";
	private static final String ON_THE_WISH_LIST_OF = "onTheWishListOf";
	
	private UserWatchableService userWatchableService;
	
	/**
	 * 
	 * <pre>
	 * 		{
	 * 			"id": 1,
	 * 			"watched": true,
	 *          "isInWishList": false,
	 *          "watchedBy": [
	 *          	{
	 * 					"id": 1,
	 * 					"email": "john.smith@mail.com",
	 * 					"firstName": "John",
	 * 					"lastName": "Smith",
	 * 					"publicProfile": true,
	 * 					"image": "http://image.png"
	 * 				}
	 *          ],
	 *         	"onTheWishListOf": [
	 *          	{
	 * 					"id": 1,
	 * 					"email": "john.smith@mail.com",
	 * 					"firstName": "John",
	 * 					"lastName": "Smith",
	 * 					"publicProfile": true,
	 * 					"image": "http://image.png"
	 * 				}
	 *          ],
	 * 			"watchable":  {
	 * 			   "id": 1,
	 * 			   "type": "Movie",
	 * 			   "name": "Sin City",
	 * 			   "image": "http://server.com/image1.png",
	 *             "releaseDate": "yyyy-MM-dd HH:mm:ss Z",
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
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(UserWatchable userWatchable, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, userWatchable.getId());
		map.put(WATCHED, userWatchable.isWatched());
		map.put(IS_IN_WISH_LIST, userWatchable.isInWishList());
		map.put(WATCHABLE, userWatchable.getWatchable());
		map.put(WATCHED_BY,
			userWatchableService.getWatchedBy(userWatchable.getUser().getFriends(), userWatchable.getWatchable()),
			MarshallerMode.SIMPLE);
		map.put(
			ON_THE_WISH_LIST_OF,
			userWatchableService.getOnTheWishListOf(userWatchable.getUser().getFriends(), userWatchable.getWatchable()),
			MarshallerMode.SIMPLE);
		return map;
	}
	
	public void setUserWatchableService(UserWatchableService userWatchableService) {
		this.userWatchableService = userWatchableService;
	}
}
