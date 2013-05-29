package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.watchable.Watchable;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public class WatchableJsonMarshaller<T extends Watchable> implements Marshaller<T, JsonMap> {
	
	private static final String ID = "id";
	private static final String TYPE = "type";
	private static final String NAME = "name";
	private static final String IMAGE = "image";
	private static final String OVERVIEW = "overview";
	private static final String GENRES = "genres";
	private static final String ACTORS = "actors";
	private static final String RELEASE_DATE = "releaseDate";
	
	/**
	 * <pre>
	 * 		{
	 * 			"id": 1,
	 * 			"type": "Movie",
	 * 			"name": "Sin City",
	 * 			"image": "http://server.com/image1.png",
	 *          "releaseDate": "yyyy-MM-dd HH:mm:ss Z",
	 * 			"overview": "Sin City is a neo-noir crime thriller based on ...",
	 * 			"trailer": "http://www.youtube.com/watch?v=YKFLrTYKIXk",
	 *          "genres": [
	 *               "Action",
	 *               "Adventure"
	 *           ],
	 *          "actors": [
	 *               "Jennifer Aniston",
	 *               "Lisa Kudrow"
	 *           ]
	 * 		}
	 * </pre>
	 * 
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(T watchable, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, watchable.getId());
		map.put(TYPE, watchable.getClass().getSimpleName());
		map.put(NAME, watchable.getName());
		map.put(IMAGE, watchable.getImageURL());
		map.put(RELEASE_DATE, watchable.getReleaseDate());
		
		if (mode.equals(MarshallerMode.COMPLETE)) {
			map.put(OVERVIEW, watchable.getOverview());
			map.put(GENRES, watchable.getGenres());
			map.put(ACTORS, watchable.getActors());
		}
		return map;
	}
	
}
