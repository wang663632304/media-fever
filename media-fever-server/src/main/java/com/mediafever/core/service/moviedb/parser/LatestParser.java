package com.mediafever.core.service.moviedb.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonArrayWrapper;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * 
 * @author Maxi Rosson
 */
public class LatestParser extends JsonParser<JsonArrayWrapper> {
	
	private static final String ID_KEY = "id";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonArrayWrapper json) throws JSONException {
		JsonObjectWrapper jsonObject = json.getJSONObject(0);
		Long latestId = jsonObject.getLong(ID_KEY);
		return latestId;
	}
}
