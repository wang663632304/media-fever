package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * 
 * @author Maxi Rosson
 */
public class InnerWatchableParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String WATCHABLE = "watchable";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new WatchableParser().parse(json.getJSONObject(WATCHABLE));
	}
	
}
