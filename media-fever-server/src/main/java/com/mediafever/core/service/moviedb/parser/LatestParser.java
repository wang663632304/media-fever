package com.mediafever.core.service.moviedb.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * 
 * @author Maxi Rosson
 */
public class LatestParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID_KEY = "id";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		Long latestId = json.getLong(ID_KEY);
		return latestId;
	}
}
