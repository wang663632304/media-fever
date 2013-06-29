package com.mediafever.core.service.push.gcm;

import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class AuthenticatedGcmMessage extends DefaultGcmMessage {
	
	private static final String REQUIRES_AUTHENTICATION_KEY_EXTRA = "requiresAuthenticationKey";
	
	public AuthenticatedGcmMessage() {
		addParameter(REQUIRES_AUTHENTICATION_KEY_EXTRA, true);
	}
}
