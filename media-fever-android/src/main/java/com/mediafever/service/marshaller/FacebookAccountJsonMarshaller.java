package com.mediafever.service.marshaller;

import com.jdroid.java.json.JsonMap;

/**
 * Facebook account JSON marshaller.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccountJsonMarshaller {
	
	private final static String USER_ID = "userId";
	private final static String ACCESS_TOKEN = "accessToken";
	private final static String EXPIRES_IN = "accessExpiresIn";
	
	public String marshall(String userId, String accessToken, Long expiresIn) {
		return doMarshall(userId, accessToken, expiresIn).toString();
	}
	
	protected JsonMap doMarshall(String userId, String accessToken, Long expiresIn) {
		JsonMap map = new JsonMap();
		map.put(USER_ID, userId);
		map.put(ACCESS_TOKEN, accessToken);
		map.put(EXPIRES_IN, expiresIn);
		return map;
	}
}
