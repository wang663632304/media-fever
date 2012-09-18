package com.mediafever.api.controller.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * 
 * @author Maxi Rosson
 */
public class UserParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String PUBLIC_PROFILE = "publicProfile";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		return new UserJson(json.getString(EMAIL), json.optString(PASSWORD), json.getString(FIRST_NAME),
				json.getString(LAST_NAME), json.optBoolean(PUBLIC_PROFILE));
	}
	
	public class UserJson {
		
		private String email;
		private String password;
		private String firstName;
		private String lastName;
		private Boolean publicProfile;
		
		public UserJson(String email, String password, String firstName, String lastName, Boolean publicProfile) {
			this.email = email;
			this.password = password;
			this.firstName = firstName;
			this.lastName = lastName;
			this.publicProfile = publicProfile;
		}
		
		public String getEmail() {
			return email;
		}
		
		public String getPassword() {
			return password;
		}
		
		public String getFirstName() {
			return firstName;
		}
		
		public String getLastName() {
			return lastName;
		}
		
		public Boolean getPublicProfile() {
			return publicProfile;
		}
		
	}
}
