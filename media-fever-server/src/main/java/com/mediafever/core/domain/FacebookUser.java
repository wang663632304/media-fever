package com.mediafever.core.domain;

import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookUser extends Entity {
	
	private String facebookId;
	private String firstName;
	private String lastName;
	
	public FacebookUser(String facebookId, String firstName, String lastName) {
		this.facebookId = facebookId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/**
	 * @return the facebookId
	 */
	public String getFacebookId() {
		return facebookId;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	public String getProfilePictureURL() {
		return "https://graph.facebook.com/" + facebookId + "/picture?type=normal";
	}
	
}
