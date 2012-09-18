package com.mediafever.api.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jdroid.java.utils.Hasher;
import com.jdroid.javaweb.exception.CommonErrorCode;
import com.mediafever.api.exception.ExceptionHandler;
import com.mediafever.context.ApplicationContext;

/**
 * 
 * @author Maxi Rosson
 */
public class SecurityFilter extends OncePerRequestFilter {
	
	private static final Log LOG = LogFactory.getLog(SecurityFilter.class);
	
	private static final String TOKEN_HEADER = "x-token";
	private static final String TIME_SYNC_HEADER = "x-time-sync";
	private static final String HEADER_TOKEN_SEPARATOR = "--";
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = request.getHeader(TOKEN_HEADER);
		String timeSync = request.getHeader(TIME_SYNC_HEADER);
		String hashGen = Hasher.SHA_1.hash(timeSync + HEADER_TOKEN_SEPARATOR
				+ ApplicationContext.get().getTokenSecret());
		
		if (hashGen.equals(token)) {
			filterChain.doFilter(request, response);
		} else {
			response.setHeader(ExceptionHandler.STATUS_CODE_HEADER,
				CommonErrorCode.INVALID_SECURITY_TOKEN.getStatusCode());
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOG.warn("Invalid or missing security token: " + token);
		}
	}
}
