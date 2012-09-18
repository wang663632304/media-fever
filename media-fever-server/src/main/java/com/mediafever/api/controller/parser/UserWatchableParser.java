package com.mediafever.api.controller.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.api.marshaller.json.UserWatchableJsonMarshaller;

/**
 * 
 * @author Maxi Rosson
 */
public class UserWatchableParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String USER_ID = "userId";
	private static final String WATCHABLE_ID = "watchableId";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new UserWatchableJson(json.optLong(USER_ID), json.optLong(WATCHABLE_ID),
				json.getBoolean(UserWatchableJsonMarshaller.WATCHED),
				json.getBoolean(UserWatchableJsonMarshaller.IS_IN_WISH_LIST));
	}
	
	public class UserWatchableJson {
		
		private Long userId;
		private Long watchableId;
		private Boolean watched;
		private Boolean isInWishList;
		
		public UserWatchableJson(Long userId, Long watchableId, Boolean watched, Boolean isInWishList) {
			this.userId = userId;
			this.watchableId = watchableId;
			this.watched = watched;
			this.isInWishList = isInWishList;
		}
		
		public Boolean isWatched() {
			return watched;
		}
		
		public Boolean isInWishList() {
			return isInWishList;
		}
		
		public Long getUserId() {
			return userId;
		}
		
		public Long getWatchableId() {
			return watchableId;
		}
		
	}
}
