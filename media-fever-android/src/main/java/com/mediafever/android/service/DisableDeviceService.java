package com.mediafever.android.service;

import android.content.Context;
import android.content.Intent;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.service.WorkerService;
import com.jdroid.android.utils.AndroidUtils;
import com.google.android.gcm.GCMRegistrar;
import com.google.inject.Inject;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class DisableDeviceService extends WorkerService {
	
	private static String userToken;
	
	@Inject
	private APIService apiService;
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		if (GCMRegistrar.isRegisteredOnServer(getApplicationContext())) {
			
			User user = SecurityContext.get().getUser();
			String userToken = user != null ? user.getUserToken() : null;
			if (userToken == null) {
				userToken = DisableDeviceService.userToken;
			}
			
			// If this fails, the device is unregistered from GCM, but still registered in the server. We could try to
			// unregister again, but it is not necessary: if the server tries to send a message to the device, it will
			// get a "NotRegistered" error message and should unregister the device.
			// DisableDeviceUseCase useCase = AbstractApplication.getInstance(DisableDeviceUseCase.class);
			// useCase.run();
			apiService.disableDevice(AndroidUtils.getInstallationId(), userToken);
			GCMRegistrar.setRegisteredOnServer(getApplicationContext(), false);
		}
		userToken = null;
	}
	
	public static void runIntentInService(Context context) {
		WorkerService.runIntentInService(context, new Intent(), DisableDeviceService.class);
	}
	
	public static void setUserToken(String userToken) {
		DisableDeviceService.userToken = userToken;
	}
}
