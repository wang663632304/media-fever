package com.mediafever.api.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jdroid.javaweb.context.AbstractApplicationContext;
import com.jdroid.javaweb.domain.Entity;

/**
 * Web Filter to add information to Log4J for logging
 * 
 */
public class Log4jFilter extends OncePerRequestFilter {
	
	private static final String APP_URL = "appURL";
	private static final String USER_ID = "userId";
	private static final String SESSION_ID = "sessionId";
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Entity user = AbstractApplicationContext.get().getSecurityContext().getUser();
		if (user != null) {
			// Add the user id to the mapped diagnostic context. May be shown using %X{userId} in the layout pattern.
			MDC.put(USER_ID, user.getId());
		}
		
		String appURL = AbstractApplicationContext.get().getAppURL();
		if (appURL == null) {
			AbstractApplicationContext.get().setApplicationURL(request.getRequestURL().toString());
			appURL = AbstractApplicationContext.get().getAppURL();
		}
		// Add the app URL to the mapped diagnostic context. May be shown using %X{appURL} in the layout pattern.
		MDC.put(APP_URL, appURL);
		
		// Add the session id to the mapped diagnostic context. May be shown using %X{sessionId} in the layout pattern.
		MDC.put(SESSION_ID, request.getSession().getId());
		
		try {
			// Continue processing the rest of the filter chain.
			filterChain.doFilter(request, response);
		} finally {
			// Remove the added elements - only if added.
			MDC.remove(USER_ID);
			MDC.remove(APP_URL);
			MDC.remove(SESSION_ID);
		}
	}
}
