package com.mediafever.service.marshaller;

import java.util.Date;
import com.jdroid.java.json.JsonMap;

/**
 * Facebook account JSON marshaller.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccountJsonMarshaller {
	
	private final static String USER_ID = "userId";
	private final static String ACCESS_TOKEN = "accessToken";
	private final static String EXPIRATION_DATE = "accessExpirationDate";
	
	public String marshall(String userId, String accessToken, Date expirationDate) {
		return doMarshall(userId, accessToken, expirationDate).toString();
	}
	
	protected JsonMap doMarshall(String userId, String accessToken, Date expiresIn) {
		JsonMap map = new JsonMap();
		map.put(USER_ID, userId);
		map.put(ACCESS_TOKEN, accessToken);
		map.put(EXPIRATION_DATE, expiresIn);
		return map;
	}
}
