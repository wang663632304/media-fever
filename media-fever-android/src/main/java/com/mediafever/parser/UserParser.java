package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class UserParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String USER_TOKEN = "userToken";
	private static final String EMAIL = "email";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String IMAGE = "image";
	private static final String PUBLIC_PROFILE = "publicProfile";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new UserImpl(json.getLong(ID), json.getString(USER_TOKEN), json.getString(EMAIL),
				json.getString(FIRST_NAME), json.getString(LAST_NAME), json.getString(IMAGE),
				json.optBoolean(PUBLIC_PROFILE));
	}
	
}
