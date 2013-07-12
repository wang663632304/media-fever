package com.mediafever.android.gcm;

import com.jdroid.android.gcm.DefaultGcmMessageResolver;
import com.jdroid.android.gcm.GcmMessageResolver;
import com.mediafever.android.AndroidApplication;
import com.mediafever.android.service.LogoutService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaFeverGcmResolver extends DefaultGcmMessageResolver {
	
	private static final GcmMessageResolver INSTANCE = new MediaFeverGcmResolver();
	
	public static GcmMessageResolver get() {
		return INSTANCE;
	}
	
	public MediaFeverGcmResolver() {
		super(MediaFeverGcmMessage.values());
	}
	
	/**
	 * @see com.jdroid.android.gcm.DefaultGcmMessageResolver#onAuthenticationRequired()
	 */
	@Override
	protected void onAuthenticationRequired() {
		// Tell the server to remove this device to that user id
		LogoutService.runLogoutService(AndroidApplication.get());
	}
	
}
