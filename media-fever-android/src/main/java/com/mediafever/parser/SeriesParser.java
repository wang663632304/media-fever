package com.mediafever.parser;

import java.util.List;
import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.watchable.Season;
import com.mediafever.domain.watchable.Series;

/**
 * 
 * @author Maxi Rosson
 */
public class SeriesParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String IMAGE = "image";
	private static final String OVERVIEW = "overview";
	private static final String ACTORS = "actors";
	private static final String GENRES = "genres";
	private static final String RELEASE_YEAR = "releaseYear";
	private static final String SEASONS = "seasons";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		List<Season> seasons = parseList(json.optJSONArray(SEASONS), new SeasonParser());
		return new Series(json.getLong(ID), json.getString(NAME), json.getString(IMAGE), json.optString(OVERVIEW),
				json.optList(ACTORS), json.optList(GENRES), json.optInt(RELEASE_YEAR), seasons);
	}
	
}
