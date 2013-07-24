package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.SocialUser;
import com.mediafever.domain.SocialUser.SocialNetwork;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialUserParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String SOCIAL_NETWORK = "socialNetwork";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String IMAGE = "image";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new SocialUser(json.getLong(ID), SocialNetwork.valueOf(json.getString(SOCIAL_NETWORK)),
				json.getString(FIRST_NAME), json.getString(LAST_NAME), json.optString(IMAGE));
	}
	
}
