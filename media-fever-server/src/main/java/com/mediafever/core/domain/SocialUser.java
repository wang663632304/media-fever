package com.mediafever.core.domain;

import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialUser extends Entity {
	
	public enum SocialNetwork {
		FACEBOOK,
		GOOGLE_PLUS;
	}
	
	private String socialId;
	private SocialNetwork socialNetwork;
	private String firstName;
	private String lastName;
	private String profilePictureURL;
	
	public SocialUser(String socialId, SocialNetwork socialNetwork, String firstName, String lastName,
			String profilePictureURL) {
		this.socialId = socialId;
		this.socialNetwork = socialNetwork;
		this.firstName = firstName;
		this.lastName = lastName;
		this.profilePictureURL = profilePictureURL;
	}
	
	/**
	 * @return the socialId
	 */
	public String getSocialId() {
		return socialId;
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
	
	/**
	 * @return the socialNetwork
	 */
	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}
	
	/**
	 * @return the profilePictureURL
	 */
	public String getProfilePictureURL() {
		return profilePictureURL;
	}
	
}
