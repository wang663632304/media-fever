package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.SocialUser;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialUserJsonMarshaller implements Marshaller<SocialUser, JsonMap> {
	
	private static final String ID = "id";
	private static final String SOCIAL_ID = "socialId";
	private static final String SOCIAL_NETWORK = "socialNetwork";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String IMAGE = "image";
	
	/**
	 * 
	 * <pre>
	 * 	 	{
	 * 			"id": 123123,
	 * 			"socialId": "socialId1",
	 * 			"socialNetwork": "FACEBOOK" 
	 * 			"firstName": "John",
	 * 			"lastName": "Smith",
	 * 			"image": "http://image.png"
	 * 		}
	 * </pre>
	 * 
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(SocialUser user, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, user.getId());
		map.put(SOCIAL_ID, user.getSocialId());
		map.put(SOCIAL_NETWORK, user.getSocialNetwork());
		map.put(FIRST_NAME, user.getFirstName());
		map.put(LAST_NAME, user.getLastName());
		map.put(IMAGE, user.getProfilePictureURL());
		return map;
	}
}
