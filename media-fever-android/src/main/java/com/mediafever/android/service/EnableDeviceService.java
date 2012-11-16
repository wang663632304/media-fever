package com.mediafever.android.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gcm.GCMRegistrar;
import com.google.inject.Inject;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.utils.IntentRetryUtils;
import com.jdroid.java.exception.ApplicationException;
import com.mediafever.android.gcm.GcmService;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class EnableDeviceService extends WorkerService {
	
	private static final String TAG = GcmService.class.getSimpleName();
	
	@Inject
	private APIService apiService;
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		if (SecurityContext.get().isAuthenticated()) {
			try {
				String registrationId = GCMRegistrar.getRegistrationId(getApplicationContext());
				apiService.enableDevice(AbstractApplication.get().getInstallationId(), registrationId);
				GCMRegistrar.setRegisteredOnServer(getApplicationContext(), true);
			} catch (ApplicationException e) {
				Log.w(TAG, "Failed to register the device on server. Will retry later.");
				IntentRetryUtils.retry(intent);
			}
		}
	}
	
	public static void runIntentInService(Context context) {
		WorkerService.runIntentInService(context, new Intent(), EnableDeviceService.class);
	}
}
