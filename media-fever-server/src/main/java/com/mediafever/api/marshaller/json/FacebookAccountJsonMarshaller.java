package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.FacebookAccount;

/**
 * {@link FacebookAccount} JSON marshaller.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccountJsonMarshaller implements Marshaller<FacebookAccount, JsonMap> {
	
	private final static String ACCESS_TOKEN = "accessToken";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object, com.jdroid.java.marshaller.MarshallerMode,
	 *      java.util.Map)
	 */
	@Override
	public JsonMap marshall(FacebookAccount facebookAccount, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(ACCESS_TOKEN, facebookAccount.getAccessToken());
		return map;
	}
	
}
