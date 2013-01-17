package com.mediafever.parser;

import org.json.JSONException;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.FriendRequest;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String SENDER = "sender";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		UserImpl sender = (UserImpl)(new UserParser().parse(json.getJSONObject(SENDER)));
		return new FriendRequest(json.optLong(ID), (UserImpl)SecurityContext.get().getUser(), sender);
	}
	
}
