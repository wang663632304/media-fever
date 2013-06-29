package com.mediafever.android.service;

import android.content.Context;
import com.google.inject.Inject;
import com.jdroid.android.gcm.AbstractGcmRegistrationService;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class EnableDeviceService extends AbstractGcmRegistrationService {
	
	@Inject
	private APIService apiService;
	
	/**
	 * @see com.jdroid.android.gcm.AbstractGcmRegistrationService#onRegisterOnServer(java.lang.String)
	 */
	@Override
	protected void onRegisterOnServer(String registrationId) {
		apiService.enableDevice(registrationId);
	}
	
	public static void runIntentInService(Context context) {
		AbstractGcmRegistrationService.runRegistrationService(context, EnableDeviceService.class);
	}
}
