package com.mediafever.api.controller.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * Parser for an user's facebook account.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccountParser extends JsonParser<JsonObjectWrapper> {
	
	private final static String USER_ID = "userId";
	private final static String ACCESS_TOKEN = "accessToken";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new FacebookAccountJson(json.getString(USER_ID), json.getString(ACCESS_TOKEN));
	}
	
	public class FacebookAccountJson {
		
		private String userId;
		private String accessToken;
		
		public FacebookAccountJson(String userId, String accessToken) {
			this.userId = userId;
			this.accessToken = accessToken;
		}
		
		/**
		 * @return the userId
		 */
		public String getUserId() {
			return userId;
		}
		
		/**
		 * @return the accessToken
		 */
		public String getAccessToken() {
			return accessToken;
		}
	}
}
