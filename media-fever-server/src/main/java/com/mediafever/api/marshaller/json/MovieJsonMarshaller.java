package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.watchable.Movie;

/**
 * 
 * @author Maxi Rosson
 */
public class MovieJsonMarshaller extends WatchableJsonMarshaller<Movie> {
	
	private static final String WATCHABLE_TAGLINE = "tagline";
	private static final String WATCHABLE_TRAILER = "trailer";
	
	/**
	 * @see com.mediafever.api.marshaller.json.WatchableJsonMarshaller#marshall(com.mediafever.core.domain.watchable.Watchable,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(Movie movie, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = super.marshall(movie, mode, extras);
		if (mode.equals(MarshallerMode.COMPLETE)) {
			map.put(WATCHABLE_TAGLINE, movie.getTagline());
			map.put(WATCHABLE_TRAILER, movie.getTrailerURL());
		}
		return map;
	}
	
}
