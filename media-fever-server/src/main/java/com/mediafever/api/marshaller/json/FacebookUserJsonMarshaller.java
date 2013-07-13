package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.FacebookUser;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookUserJsonMarshaller implements Marshaller<FacebookUser, JsonMap> {
	
	private static final String ID = "id";
	private static final String FACEBOOK_ID = "facebookId";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String IMAGE = "image";
	
	/**
	 * 
	 * <pre>
	 * 		{
	 * 			"id": 1,
	 * 			"firstName": "John",
	 * 			"lastName": "Smith",
	 * 			"image": "http://image.png"
	 * 
	 * 		},
	 * 	 	{
	 * 			"facebookId": "facebookId1",
	 * 			"firstName": "John",
	 * 			"lastName": "Smith",
	 * 			"image": "http://image.png"
	 * 
	 * 		}
	 * </pre>
	 * 
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(FacebookUser user, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		if (user.getId() != null) {
			map.put(ID, user.getId());
		} else {
			map.put(FACEBOOK_ID, user.getFacebookId());
		}
		map.put(FIRST_NAME, user.getFirstName());
		map.put(LAST_NAME, user.getLastName());
		map.put(IMAGE, user.getProfilePictureURL());
		return map;
	}
}
