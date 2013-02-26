package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.session.MediaSessionUser;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionUserJsonMarshaller implements Marshaller<MediaSessionUser, JsonMap> {
	
	private static final String USER = "user";
	private static final String ACCEPTED = "accepted";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(MediaSessionUser mediaSessionUser, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(USER, mediaSessionUser.getUser());
		map.put(ACCEPTED, mediaSessionUser.isAccepted());
		return map;
	}
}
