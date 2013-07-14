package com.mediafever.domain;

import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.UriFileContent;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookUser extends Entity {
	
	private String facebookId;
	private String firstName;
	private String lastName;
	private FileContent image;
	
	public FacebookUser(Long id, String facebookId, String firstName, String lastName, String imageURL) {
		super(id);
		this.facebookId = facebookId;
		this.firstName = firstName;
		this.lastName = lastName;
		image = new UriFileContent(imageURL);
	}
	
	public Boolean isMediaFeverUser() {
		return getId() != null;
	}
	
	public String getFullname() {
		return firstName + " " + lastName;
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
	
	public FileContent getImage() {
		return image;
	}
	
}
