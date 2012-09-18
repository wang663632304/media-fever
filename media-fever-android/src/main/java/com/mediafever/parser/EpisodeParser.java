package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.watchable.Episode;

/**
 * 
 * @author Maxi Rosson
 */
public class EpisodeParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String NUMBER = "number";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new Episode(json.getLong(ID), json.getString(NAME), null, null, null, json.getInt(NUMBER));
	}
	
}
