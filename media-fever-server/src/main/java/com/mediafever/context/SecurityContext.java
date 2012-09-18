package com.mediafever.context;

import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.javaweb.context.AbstractSecurityContext;
import com.jdroid.javaweb.exception.CommonErrorCode;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;
import com.mediafever.core.domain.User;
import com.mediafever.core.repository.UserRepository;

public class SecurityContext extends AbstractSecurityContext<User> {
	
	private UserRepository userRepository;
	
	/**
	 * @see com.jdroid.javaweb.context.AbstractSecurityContext#loadUser(java.lang.String)
	 */
	@Override
	protected User verifyToken(String userToken) throws InvalidAuthenticationException {
		try {
			User user = userRepository.getByUserToken(userToken);
			return user;
		} catch (ObjectNotFoundException e) {
			throw new InvalidAuthenticationException(CommonErrorCode.INVALID_USER_TOKEN);
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.context.AbstractSecurityContext#verifyPassword(java.lang.String, java.lang.String)
	 */
	@Override
	protected User verifyPassword(String email, String password) throws InvalidAuthenticationException {
		try {
			User user = userRepository.getByEmail(email);
			user.verifyPassword(password);
			return user;
		} catch (ObjectNotFoundException e) {
			throw new InvalidAuthenticationException(CommonErrorCode.INVALID_CREDENTIALS);
		}
	}
	
	/**
	 * @param userRepository The user repository
	 */
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
