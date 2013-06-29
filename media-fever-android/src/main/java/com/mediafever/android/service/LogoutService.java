package com.mediafever.android.service;

import android.content.Context;
import android.content.Intent;
import com.google.inject.Inject;
import com.jdroid.android.service.WorkerService;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class LogoutService extends WorkerService {
	
	private static final String USER_ID_KEY = "userId";
	
	@Inject
	private APIService apiService;
	
	/**
	 * @see com.jdroid.android.service.WorkerService#doExecute(android.content.Intent)
	 */
	@Override
	protected void doExecute(Intent intent) {
		Long userId = intent.getLongExtra(USER_ID_KEY, 0L);
		if (userId != 0) {
			apiService.logout(userId);
		}
	}
	
	public static void runLogoutService(Context context) {
		runLogoutService(context, null);
	}
	
	public static void runLogoutService(Context context, Long userId) {
		Intent intent = new Intent();
		intent.putExtra(USER_ID_KEY, userId);
		WorkerService.runIntentInService(context, intent, LogoutService.class);
	}
}
