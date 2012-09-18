package com.mediafever.parser;

import java.util.List;
import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Episode;
import com.mediafever.domain.watchable.Season;

/**
 * 
 * @author Maxi Rosson
 */
public class SeasonParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String NUMBER = "number";
	private static final String EPISODES_USER_WATCHABLES = "episodesUserWatchables";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		List<UserWatchable<Episode>> episodesUserWatchables = parseList(json.optJSONArray(EPISODES_USER_WATCHABLES),
			new UserWatchableParser());
		return new Season(json.getLong(ID), json.getInt(NUMBER), episodesUserWatchables);
	}
	
}
