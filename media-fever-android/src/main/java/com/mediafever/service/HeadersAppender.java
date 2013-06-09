package com.mediafever.service;

import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.java.http.HttpResponseWrapper;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.WebService;
import com.jdroid.java.utils.Hasher;
import com.mediafever.context.ApplicationContext;

/**
 * 
 * @author Maxi Rosson
 */
public class HeadersAppender implements HttpWebServiceProcessor {
	
	private static final String API_VERSION_HEADER = "api-version";
	private static final String API_VERSION_HEADER_VALUE = "1.0";
	
	private static final String USER_AGENT_HEADER_VALUE = "android";
	
	private static final String TIME_SYNC_HEADER = "x-time-sync";
	private static final String TOKEN_HEADER = "x-token";
	private static final String HEADER_TOKEN_SEPARATOR = "--";
	public static final String USER_TOKEN_HEADER = "x-user-token";
	
	private static final HeadersAppender INSTANCE = new HeadersAppender();
	
	private HeadersAppender() {
		// nothing...
	}
	
	public static HeadersAppender get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.java.http.HttpWebServiceProcessor#beforeExecute(com.jdroid.java.http.WebService)
	 */
	@Override
	public void beforeExecute(WebService webService) {
		
		// User Agent header
		webService.setUserAgent(USER_AGENT_HEADER_VALUE);
		
		// API Version header
		webService.addHeader(API_VERSION_HEADER, API_VERSION_HEADER_VALUE);
		
		webService.addHeader(WebService.CONTENT_TYPE_HEADER, MimeType.JSON.toString());
		webService.addHeader(WebService.ACCEPT_HEADER, MimeType.JSON.toString());
		webService.addHeader(WebService.ACCEPT_ENCODING_HEADER, WebService.GZIP_ENCODING);
		
		// Time sync header. (time in milliseconds)
		String timeSync = String.valueOf(System.currentTimeMillis());
		webService.addHeader(TIME_SYNC_HEADER, timeSync);
		
		// Security Token
		webService.addHeader(TOKEN_HEADER,
			Hasher.SHA_1.hash(timeSync + HEADER_TOKEN_SEPARATOR + ApplicationContext.get().getServerTokenSecret()));
		
		// User Token
		User user = SecurityContext.get().getUser();
		if (user != null) {
			webService.addHeader(USER_TOKEN_HEADER, user.getUserToken());
		}
		
	}
	
	/**
	 * @see com.jdroid.java.http.HttpWebServiceProcessor#afterExecute(com.jdroid.java.http.WebService,
	 *      com.jdroid.java.http.HttpResponseWrapper)
	 */
	@Override
	public void afterExecute(WebService webService, HttpResponseWrapper httpResponse) {
		// Do Nothing
	}
	
}
