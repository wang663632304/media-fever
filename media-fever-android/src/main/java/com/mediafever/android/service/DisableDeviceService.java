package com.mediafever.android.service;

import java.io.IOException;
import org.slf4j.Logger;
import android.content.Context;
import android.content.Intent;
import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Inject;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.service.WorkerService;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class DisableDeviceService extends WorkerService {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(DisableDeviceService.class);
	
	private static final String USER_TOKEN = "userToken";
	
	@Inject
	private APIService apiService;
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		try {
			GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);
			googleCloudMessaging.unregister();
		} catch (IOException e) {
			LOGGER.warn("Failed to unregister the device on gcm.");
		}
		
		// If this fails, the device is unregistered from GCM, but still registered in the server. We could try
		// to unregister again, but it is not necessary: if the server tries to send a message to the device, it
		// will get a "NotRegistered" error message and should unregister the device.
		String userToken = intent.getStringExtra(USER_TOKEN);
		apiService.disableDevice(AbstractApplication.get().getInstallationId(), userToken);
		GCMRegistrar.setRegisteredOnServer(getApplicationContext(), false);
	}
	
	public static void runIntentInService(Context context, String userToken) {
		Intent intent = new Intent();
		intent.putExtra(USER_TOKEN, userToken);
		WorkerService.runIntentInService(context, intent, DisableDeviceService.class);
	}
}
