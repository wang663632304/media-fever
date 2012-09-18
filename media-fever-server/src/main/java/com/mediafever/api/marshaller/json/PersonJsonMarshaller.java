package com.mediafever.api.marshaller.json;

import java.util.Map;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.core.domain.watchable.Person;

/**
 * 
 * @author Maxi Rosson
 */
public class PersonJsonMarshaller implements Marshaller<Person, String> {
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public String marshall(Person person, MarshallerMode mode, Map<String, String> extras) {
		return person.getFullname();
	}
}
