package com.mediafever.api.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jdroid.java.collections.Lists;
import com.jdroid.javaweb.context.AbstractApplicationContext;
import com.jdroid.javaweb.exception.CommonErrorCode;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;
import com.mediafever.api.controller.AuthenticationController;
import com.mediafever.api.controller.DeviceController;
import com.mediafever.api.controller.UserController;
import com.mediafever.api.exception.ExceptionHandler;

/**
 * Filter used to verify that requests come from an authenticated user when accessing to content that requires
 * authentication.
 * 
 * @author Maxi Rosson
 */
public class AuthenticationFilter extends OncePerRequestFilter {
	
	private static final Log LOG = LogFactory.getLog(AuthenticationFilter.class);
	
	private static final String USER_TOKEN_HEADER = "x-user-token";
	
	private List<String> allowedPaths = Lists.newArrayList(AuthenticationController.API_AUTH_PATH,
		UserController.API_USERS_PATH, DeviceController.API_DEVICES_PATH);
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if (requiresAuthentication(request) && !isAuthenticated(request)) {
			response.setHeader(ExceptionHandler.STATUS_CODE_HEADER, CommonErrorCode.INVALID_USER_TOKEN.getStatusCode());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			
			LOG.info("Session id: " + request.getSession().getId());
			
			filterChain.doFilter(request, response);
			
			AbstractApplicationContext.get().getSecurityContext().invalidate();
			request.getSession().invalidate();
		}
	}
	
	/**
	 * Checks whether the request requires user authentication to grant access to the requested content.
	 * 
	 * @param request The {@link HttpServletRequest} to check.
	 * @return If authentication is needed.
	 */
	private Boolean requiresAuthentication(HttpServletRequest request) {
		for (String each : allowedPaths) {
			if (request.getPathInfo().startsWith(each)) {
				return false;
			}
		}
		return true;
	}
	
	private Boolean isAuthenticated(HttpServletRequest request) {
		String userToken = request.getHeader(USER_TOKEN_HEADER);
		try {
			AbstractApplicationContext.get().getSecurityContext().authenticateUser(userToken);
			return true;
		} catch (InvalidAuthenticationException e) {
			LOG.warn("User with token " + userToken + " NOT authenticated.");
			return false;
		}
	}
}
