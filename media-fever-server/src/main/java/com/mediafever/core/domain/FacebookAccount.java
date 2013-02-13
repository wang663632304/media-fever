package com.mediafever.core.domain;

import java.util.Date;
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
	
	private Date accessExpirationDate;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private FacebookAccount() {
		// Required by Hibernate.
	}
	
	public FacebookAccount(String userId, String accessToken, Date accessExpirationDate) {
		this.userId = userId;
		this.accessToken = accessToken;
		this.accessExpirationDate = accessExpirationDate;
	}
	
	/**
	 * Updates the account with a new access token and expiration date.
	 * 
	 * @param accessToken
	 * @param accessExpirationDate
	 */
	public void update(String accessToken, Date accessExpirationDate) {
		this.accessToken = accessToken;
		this.accessExpirationDate = accessExpirationDate;
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
	 * @return the access expiration date for the token.
	 */
	public Date getAccessExpirationDate() {
		return accessExpirationDate;
	}
}
