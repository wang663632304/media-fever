package com.mediafever.service.marshaller;

import com.jdroid.java.json.JsonMap;

/**
 * 
 * @author Maxi Rosson
 */
public class DeviceJsonMarshaller {
	
	private static final String INSTALLATION_ID = "installationId";
	private static final String REGISTRATION_ID = "registrationId";
	
	public String marshall(String installationId, String registrationId) {
		return doMarshall(installationId, registrationId).toString();
	}
	
	protected JsonMap doMarshall(String installationId, String registrationId) {
		JsonMap map = new JsonMap();
		map.put(INSTALLATION_ID, installationId);
		map.put(REGISTRATION_ID, registrationId);
		return map;
	}
}
