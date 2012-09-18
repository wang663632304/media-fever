package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.User;

/**
 * 
 * @author Maxi Rosson
 */
public class UserJsonMarshaller implements Marshaller<User, JsonMap> {
	
	private static final String ID = "id";
	private static final String EMAIL = "email";
	private static final String USER_TOKEN = "userToken";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String PUBLIC_PROFILE = "publicProfile";
	private static final String IMAGE = "image";
	
	/**
	 * 
	 * <pre>
	 * 		{
	 * 			"id": 1,
	 * 			"email": "john.smith@mail.com",
	 *          "userToken": "2342342342342342342342343",
	 * 			"firstName": "John",
	 * 			"lastName": "Smith",
	 * 			"publicProfile": true,
	 * 			"image": "http://image.png"
	 * 
	 * 		}
	 * </pre>
	 * 
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(User user, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, user.getId());
		map.put(EMAIL, user.getEmail());
		map.put(FIRST_NAME, user.getFirstName());
		map.put(LAST_NAME, user.getLastName());
		map.put(IMAGE, user.getImageUrl());
		map.put(PUBLIC_PROFILE, user.hasPublicProfile());
		
		if (mode.equals(MarshallerMode.COMPLETE)) {
			map.put(USER_TOKEN, user.getUserToken());
		}
		return map;
	}
}
