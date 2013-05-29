package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.watchable.Movie;

/**
 * 
 * @author Maxi Rosson
 */
public class MovieParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String TAGLINE = "tagline";
	private static final String IMAGE = "image";
	private static final String OVERVIEW = "overview";
	private static final String TRAILER = "trailer";
	private static final String ACTORS = "actors";
	private static final String GENRES = "genres";
	private static final String RELEASE_DATE = "releaseDate";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new Movie(json.getLong(ID), json.getString(NAME), json.optString(TAGLINE), json.getString(IMAGE),
				json.optString(OVERVIEW), json.optString(TRAILER), json.optList(ACTORS), json.optList(GENRES),
				json.optDate(RELEASE_DATE));
	}
	
}
