package com.mediafever.service.marshaller;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class UserJsonMarshaller implements Marshaller<UserImpl, JsonMap> {
	
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String PUBLIC_PROFILE = "publicProfile";
	
	/**
	 * 
	 * <pre>
	 * 		{
	 * 			"email": "john.smith@mail.com",
	 * 			"password": "password",
	 * 			"firstName": "John",
	 * 			"lastName": "Smith"
	 * 		}
	 * </pre>
	 * 
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(UserImpl user, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(EMAIL, user.getEmail());
		map.put(PASSWORD, user.getPassword());
		map.put(FIRST_NAME, user.getFirstName());
		map.put(LAST_NAME, user.getLastName());
		map.put(PUBLIC_PROFILE, user.hasPublicProfile());
		return map;
	}
}
