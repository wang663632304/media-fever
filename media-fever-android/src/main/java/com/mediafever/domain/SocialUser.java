package com.mediafever.domain;

import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.UriFileContent;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialUser extends Entity {
	
	public enum SocialNetwork {
		FACEBOOK,
		GOOGLE_PLUS;
	}
	
	private SocialNetwork socialNetwork;
	private String firstName;
	private String lastName;
	private FileContent image;
	
	public SocialUser(Long id, SocialNetwork socialNetwork, String firstName, String lastName, String imageURL) {
		super(id);
		this.socialNetwork = socialNetwork;
		this.firstName = firstName;
		this.lastName = lastName;
		image = new UriFileContent(imageURL);
	}
	
	public String getFullname() {
		return firstName + " " + lastName;
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
	
	public FileContent getImage() {
		return image;
	}
	
	/**
	 * @return the socialNetwork
	 */
	public SocialNetwork getSocialNetwork() {
		return socialNetwork;
	}
	
}
