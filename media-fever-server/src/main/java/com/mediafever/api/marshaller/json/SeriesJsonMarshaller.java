package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.watchable.Series;

/**
 * 
 * @author Maxi Rosson
 */
public class SeriesJsonMarshaller extends WatchableJsonMarshaller<Series> {
	
	private static final String SEASONS = "seasons";
	
	/**
	 * @see com.mediafever.api.marshaller.json.WatchableJsonMarshaller#marshall(com.mediafever.core.domain.watchable.Watchable,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(Series series, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = super.marshall(series, mode, extras);
		if (mode.equals(MarshallerMode.COMPLETE)) {
			map.put(SEASONS, series.getSeasons());
		}
		return map;
	}
}
