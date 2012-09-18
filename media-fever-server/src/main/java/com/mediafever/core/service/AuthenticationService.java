package com.mediafever.core.service;

import org.springframework.stereotype.Service;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.User;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class AuthenticationService {
	
	/**
	 * @param email The user's email
	 * @param password The user's password
	 * @return The User
	 * @throws InvalidAuthenticationException Thrown if the email and/or the password are invalid
	 */
	public User loginUser(String email, String password) throws InvalidAuthenticationException {
		return ApplicationContext.get().getSecurityContext().authenticateUser(email, password);
	}
}
