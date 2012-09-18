package com.mediafever.service.marshaller;

import com.jdroid.java.json.JsonMap;

/**
 * 
 * @author Maxi Rosson
 */
public class LoginJsonMarshaller {
	
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	
	public String marshall(String email, String password) {
		JsonMap map = new JsonMap();
		map.put(EMAIL, email);
		map.put(PASSWORD, password);
		return map.toString();
	}
}
