package com.mediafever.domain.social;

import java.util.Date;

/**
 * Represents an user's Facebook account.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccount {
	
	private String accessToken;
	private Date accessExpirationDate;
	
	public FacebookAccount(String accessToken, Date accessExpirationDate) {
		this.accessToken = accessToken;
		this.accessExpirationDate = accessExpirationDate;
	}
	
	/**
	 * @return the access expiration date
	 */
	public Date getAccessExpirationDate() {
		return accessExpirationDate;
	}
	
	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
}
