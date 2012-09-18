package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String TYPE = "type";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		WatchableType watchableType = WatchableType.find(json.getString(TYPE));
		return watchableType.getParser().parse(json);
	}
	
}
