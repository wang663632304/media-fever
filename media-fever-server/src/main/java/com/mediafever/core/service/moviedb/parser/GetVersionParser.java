package com.mediafever.core.service.moviedb.parser;

import java.util.List;
import java.util.Map;
import org.json.JSONException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdroid.java.parser.json.JsonArrayWrapper;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.core.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class GetVersionParser extends JsonParser<JsonArrayWrapper> {
	
	private static final String ID_KEY = "id";
	private static final String LAST_MODIFIED_AT_KEY = "last_modified_at";
	
	private Map<Long, Watchable> moviesMap = Maps.newHashMap();
	
	public GetVersionParser(List<Watchable> movies) {
		for (Watchable movie : movies) {
			moviesMap.put(movie.getExternalId(), movie);
		}
	}
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonArrayWrapper json) throws JSONException {
		List<Long> externalIds = Lists.newArrayList();
		int length = json.length();
		for (int i = 0; i < length; i++) {
			JsonObjectWrapper jsonObject = json.getJSONObject(i);
			Watchable movie = moviesMap.get(jsonObject.getLong(ID_KEY));
			Long lastupdated = jsonObject.getDate(LAST_MODIFIED_AT_KEY, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT).getTime();
			if (lastupdated > movie.getLastupdated()) {
				externalIds.add(movie.getExternalId());
			}
		}
		return externalIds;
	}
}
