package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.FriendRequest;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestJsonMarshaller implements Marshaller<FriendRequest, JsonMap> {
	
	private static final String ID = "id";
	private static final String SENDER = "sender";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(FriendRequest friendRequest, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, friendRequest.getId());
		map.put(SENDER, friendRequest.getSender());
		return map;
	}
}
