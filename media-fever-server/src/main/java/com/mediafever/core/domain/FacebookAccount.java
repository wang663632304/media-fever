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
	
	private Long accessExpiresIn;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private FacebookAccount() {
		// Required by Hibernate.
	}
	
	public FacebookAccount(String userId, String accessToken, Long accessExpiresIn) {
		this.userId = userId;
		this.accessToken = accessToken;
		this.accessExpiresIn = accessExpiresIn;
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
	
	/**
	 * @return the accessExpiresIn
	 */
	public Long getAccessExpiresIn() {
		return accessExpiresIn;
	}
}
