package com.mediafever.domain.social;

/**
 * Represents an user's Facebook account.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccount {
	
	private String accessToken;
	private Long accessExpiresIn;
	
	public FacebookAccount(String accessToken, Long accessExpiresIn) {
		this.accessToken = accessToken;
		this.accessExpiresIn = accessExpiresIn;
	}
	
	/**
	 * @return the accessExpiresIn
	 */
	public Long getAccessExpiresIn() {
		return accessExpiresIn;
	}
	
	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}
}
