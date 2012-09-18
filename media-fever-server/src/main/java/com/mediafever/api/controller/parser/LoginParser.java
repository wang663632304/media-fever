package com.mediafever.api.controller.parser;

import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;

/**
 * 
 * @author Maxi Rosson
 */
public class LoginParser extends DeviceParser {
	
	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	
	private String email;
	private String password;
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		super.parse(json);
		email = json.getString(EMAIL);
		password = json.getString(PASSWORD);
		return null;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
}
