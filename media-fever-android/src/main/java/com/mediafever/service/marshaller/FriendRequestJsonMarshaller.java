package com.mediafever.service.marshaller;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.domain.FriendRequest;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestJsonMarshaller implements Marshaller<FriendRequest, JsonMap> {
	
	private static final String USER_ID = "userId";
	private static final String SENDER_ID = "senderId";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(FriendRequest friendRequest, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(FriendRequestJsonMarshaller.USER_ID, friendRequest.getUser().getId());
		map.put(FriendRequestJsonMarshaller.SENDER_ID, friendRequest.getSender().getId());
		return map;
	}
}
