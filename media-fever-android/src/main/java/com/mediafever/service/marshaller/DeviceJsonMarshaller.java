package com.mediafever.service.marshaller;

import com.jdroid.java.json.JsonMap;

/**
 * 
 * @author Maxi Rosson
 */
public class DeviceJsonMarshaller {
	
	private static final String REGISTRATION_ID = "registrationId";
	
	public String marshall(String registrationId) {
		return doMarshall(registrationId).toString();
	}
	
	protected JsonMap doMarshall(String registrationId) {
		JsonMap map = new JsonMap();
		map.put(REGISTRATION_ID, registrationId);
		return map;
	}
}
