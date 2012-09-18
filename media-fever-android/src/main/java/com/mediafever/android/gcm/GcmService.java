package com.mediafever.android.gcm;

import android.content.Context;
import android.content.Intent;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.gcm.AbstractGcmService;
import com.jdroid.android.utils.ToastUtils;
import com.mediafever.R;
import com.mediafever.android.service.DisableDeviceService;
import com.mediafever.android.service.EnableDeviceService;

/**
 * 
 * @author Maxi Rosson
 */
public class GcmService extends AbstractGcmService {
	
	/**
	 * @see com.google.android.gcm.GCMBaseIntentService#onRegistered(android.content.Context, java.lang.String)
	 */
	@Override
	public void onRegistered(Context context, String registrationId) {
		EnableDeviceService.runIntentInService(context);
	}
	
	/**
	 * @see com.google.android.gcm.GCMBaseIntentService#onUnregistered(android.content.Context, java.lang.String)
	 */
	@Override
	public void onUnregistered(Context context, String registrationId) {
		DisableDeviceService.runIntentInService(context);
	}
	
	/**
	 * @see com.jdroid.android.gcm.AbstractGcmService#onMissingGoogleAccountError(android.content.Context)
	 */
	@Override
	public void onMissingGoogleAccountError(Context context) {
		ToastUtils.showToastOnUIThread(R.string.onMissingGoogleAccountError);
	}
	
	/**
	 * @see com.google.android.gcm.GCMBaseIntentService#onMessage(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onMessage(Context context, Intent intent) {
		if (SecurityContext.get().isAuthenticated()) {
			GcmMessage gcmMessage = GcmMessage.find(intent);
			if (gcmMessage != null) {
				gcmMessage.handle(intent);
			}
		}
	}
}
