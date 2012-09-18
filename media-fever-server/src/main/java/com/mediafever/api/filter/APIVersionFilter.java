package com.mediafever.api.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import com.jdroid.javaweb.exception.CommonErrorCode;
import com.mediafever.api.exception.ExceptionHandler;
import com.mediafever.context.ApplicationContext;

public class APIVersionFilter extends OncePerRequestFilter {
	
	private static final Log LOG = LogFactory.getLog(APIVersionFilter.class);
	
	private static final String API_VERSION_HEADER = "api-version";
	
	/**
	 * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String version = request.getHeader(API_VERSION_HEADER);
		if (ApplicationContext.get().getApiVersion().equals(version)) {
			response.setHeader(ExceptionHandler.STATUS_CODE_HEADER, ExceptionHandler.OK_STATUS_CODE_HEADER_VALUE);
			response.setStatus(HttpServletResponse.SC_OK);
			filterChain.doFilter(request, response);
		} else {
			response.setHeader(ExceptionHandler.STATUS_CODE_HEADER, CommonErrorCode.INVALID_API_VERSION.getStatusCode());
			response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
			LOG.warn("Invalid or missing " + API_VERSION_HEADER + " header");
		}
	}
}
