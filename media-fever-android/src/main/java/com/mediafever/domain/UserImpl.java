package com.mediafever.domain;

import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.UriFileContent;
import com.jdroid.android.domain.User;

/**
 * 
 * @author Maxi Rosson
 */
public class UserImpl extends Entity implements User {
	
	private String email;
	private String password;
	private String userToken;
	private String firstName;
	private String lastName;
	private FileContent image;
	private Boolean publicProfile;
	
	public UserImpl(String email, String password, String firstName, String lastName, String imageURL,
			Boolean publicProfile) {
		this(null, null, email, firstName, lastName, imageURL, publicProfile);
		this.password = password;
	}
	
	/**
	 * Constructor
	 * 
	 * @param id The id
	 * @param userToken The user's token
	 * @param firstName The user's firstName name
	 * @param lastName The user's lastName name
	 * @param email The user's email
	 * @param imageURL The user's avatar image URL
	 * @param publicProfile The user's publicProfile flag
	 */
	public UserImpl(Long id, String userToken, String email, String firstName, String lastName, String imageURL,
			Boolean publicProfile) {
		super(id);
		this.userToken = userToken;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		image = new UriFileContent(imageURL);
		this.publicProfile = publicProfile;
	}
	
	public void modify(String email, String password, String firstName, String lastName, String imageURL,
			Boolean publicProfile) {
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		image = new UriFileContent(imageURL);
		this.publicProfile = publicProfile;
	}
	
	/**
	 * @see com.jdroid.android.domain.User#getUserName()
	 */
	@Override
	public String getUserName() {
		return email;
	}
	
	/**
	 * @see com.jdroid.android.domain.User#getFullname()
	 */
	@Override
	public String getFullname() {
		return firstName + " " + lastName;
	}
	
	/**
	 * @see com.jdroid.android.domain.User#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}
	
	/**
	 * @see com.jdroid.android.domain.User#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @see com.jdroid.android.domain.User#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFullname();
	}
	
	/**
	 * @see com.jdroid.android.domain.User#getUserToken()
	 */
	@Override
	public String getUserToken() {
		return userToken;
	}
	
	public String getPassword() {
		return password;
	}
	
	/**
	 * @see com.jdroid.android.domain.User#getImage()
	 */
	@Override
	public FileContent getImage() {
		return image;
	}
	
	public Boolean hasPublicProfile() {
		return publicProfile;
	}
}
