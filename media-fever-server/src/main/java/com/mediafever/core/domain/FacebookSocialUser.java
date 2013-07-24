package com.mediafever.core.domain;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookSocialUser extends SocialUser {
	
	public FacebookSocialUser(String socialId, String firstName, String lastName) {
		super(socialId, SocialNetwork.FACEBOOK, firstName, lastName, "https://graph.facebook.com/" + socialId
				+ "/picture?type=normal");
	}
}
