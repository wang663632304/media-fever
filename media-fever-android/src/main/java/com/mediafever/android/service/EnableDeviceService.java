package com.mediafever.android.service;

import org.slf4j.Logger;
import android.content.Context;
import android.content.Intent;
import com.google.android.gcm.GCMRegistrar;
import com.google.inject.Inject;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.utils.IntentRetryUtils;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class EnableDeviceService extends WorkerService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(EnableDeviceService.class);
	
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
				LOGGER.warn("Failed to register the device on server. Will retry later.");
				IntentRetryUtils.retry(intent);
			}
		}
	}
	
	public static void runIntentInService(Context context) {
		WorkerService.runIntentInService(context, new Intent(), EnableDeviceService.class);
	}
}
