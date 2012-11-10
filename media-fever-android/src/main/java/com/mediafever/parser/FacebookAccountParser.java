package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.social.FacebookAccount;

/**
 * JSON parser for {@link FacebookAccount}.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccountParser extends JsonParser<JsonObjectWrapper> {
	
	private final static String ACCESS_TOKEN = "accessToken";
	private final static String EXPIRES_IN = "accessExpiresIn";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new FacebookAccount(json.getString(ACCESS_TOKEN), json.getLong(EXPIRES_IN));
	}
	
}
