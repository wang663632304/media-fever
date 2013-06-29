package com.mediafever.android.service;

import java.io.IOException;
import org.slf4j.Logger;
import android.content.Context;
import android.content.Intent;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Inject;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.gcm.GcmPreferences;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.utils.IntentRetryUtils;
import com.jdroid.java.exception.ApplicationException;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.context.ApplicationContext;
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
		try {
			GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
			String registrationId = googleCloudMessaging.register(ApplicationContext.get().getGoogleProjectId());
			GcmPreferences.setRegistrationId(getApplicationContext(), registrationId);
			
			apiService.enableDevice(AbstractApplication.get().getInstallationId(), registrationId);
			GcmPreferences.setRegisteredOnServer(getApplicationContext(), true);
		} catch (ApplicationException e) {
			LOGGER.warn("Failed to register the device on server. Will retry later.");
			IntentRetryUtils.retry(intent);
		} catch (IOException e) {
			LOGGER.warn("Failed to register the device on gcm. Will retry later.");
			IntentRetryUtils.retry(intent);
		}
	}
	
	public static void runIntentInService(Context context) {
		WorkerService.runIntentInService(context, new Intent(), EnableDeviceService.class);
	}
}
