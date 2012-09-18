package com.mediafever.api.controller.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String USER_ID = "userId";
	private static final String SENDER_ID = "senderId";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new FriendRequestJson(json.getLong(USER_ID), json.getLong(SENDER_ID));
	}
	
	public class FriendRequestJson {
		
		private Long userId;
		private Long senderId;
		
		public FriendRequestJson(Long userId, Long senderId) {
			this.userId = userId;
			this.senderId = senderId;
		}
		
		public Long getUserId() {
			return userId;
		}
		
		public Long getSenderId() {
			return senderId;
		}
		
	}
}
