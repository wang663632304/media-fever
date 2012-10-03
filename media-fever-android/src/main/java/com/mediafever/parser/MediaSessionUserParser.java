package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.android.domain.User;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.session.MediaSessionUser;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionUserParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String USER = "user";
	private static final String PENDING_THUMBS_UP = "pendingThumbsUp";
	private static final String PENDING_THUMBS_DOWN = "pendingThumbsDown";
	private static final String ACCEPTED = "accepted";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		User user = (User)new UserParser().parse(json.getJSONObject(USER));
		return new MediaSessionUser(user, json.optBoolean(ACCEPTED), json.getInt(PENDING_THUMBS_UP),
				json.getInt(PENDING_THUMBS_DOWN));
	}
	
}
