package com.mediafever.core.domain.watchable;

import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class Person extends Entity {
	
	private String fullname;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Person() {
		// Do nothing, is required by hibernate
	}
	
	public Person(String fullname) {
		this.fullname = fullname;
	}
	
	public String getFullname() {
		return fullname;
	}
}
