package com.mediafever.parser;

import java.util.List;
import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.UserImpl;
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
	private static final String WATCHED_BY = "watchedBy";
	private static final String ON_THE_WISH_LIST_OF = "onTheWishListOf";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		Watchable watchable = (Watchable)new WatchableParser().parse(json.getJSONObject(WATCHABLE));
		UserParser userParser = new UserParser();
		List<UserImpl> watchedBy = parseList(json.optJSONArray(WATCHED_BY), userParser);
		List<UserImpl> onTheWishListOf = parseList(json.optJSONArray(ON_THE_WISH_LIST_OF), userParser);
		return new UserWatchable<Watchable>(json.optLong(ID), json.getBoolean(WATCHED),
				json.getBoolean(IS_IN_WISH_LIST), watchable, watchedBy, onTheWishListOf);
	}
}
