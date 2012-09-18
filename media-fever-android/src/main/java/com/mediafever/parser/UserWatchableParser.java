package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class UserWatchableParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	public static final String WATCHED = "watched";
	public static final String IS_IN_WISH_LIST = "isInWishList";
	private static final String WATCHABLE = "watchable";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new UserWatchable<Watchable>(json.optLong(ID), json.getBoolean(WATCHED),
				json.getBoolean(IS_IN_WISH_LIST), (Watchable)new WatchableParser().parse(json.getJSONObject(WATCHABLE)));
	}
	
}
