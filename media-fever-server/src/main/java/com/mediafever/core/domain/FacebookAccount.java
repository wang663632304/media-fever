package com.mediafever.core.domain;

import com.jdroid.javaweb.domain.Entity;

/**
 * Facebook account an {@link User} may be linked to.
 * 
 * @author Estefanía Caravatti
 */
@javax.persistence.Entity
public class FacebookAccount extends Entity {
	
	private String userId;
	
	private String accessToken;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private FacebookAccount() {
		// Required by Hibernate.
	}
	
	public FacebookAccount(String userId, String accessToken) {
		this.userId = userId;
		this.accessToken = accessToken;
	}
	
	public String getProfilePictureURL() {
		return "https://graph.facebook.com/" + userId + "/picture?type=normal";
	}
	
	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
}
