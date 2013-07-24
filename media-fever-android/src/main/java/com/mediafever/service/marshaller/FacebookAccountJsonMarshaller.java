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
	
	public String marshall(String userId, String accessToken) {
		return doMarshall(userId, accessToken).toString();
	}
	
	protected JsonMap doMarshall(String userId, String accessToken) {
		JsonMap map = new JsonMap();
		map.put(USER_ID, userId);
		map.put(ACCESS_TOKEN, accessToken);
		return map;
	}
}
