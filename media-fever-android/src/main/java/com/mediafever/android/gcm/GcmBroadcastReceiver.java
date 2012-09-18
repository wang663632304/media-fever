package com.mediafever.android.gcm;

import android.content.Context;
import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * 
 * @author Maxi Rosson
 */
public class GcmBroadcastReceiver extends GCMBroadcastReceiver {
	
	/**
	 * @see com.google.android.gcm.GCMBroadcastReceiver#getGCMIntentServiceClassName(android.content.Context)
	 */
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return GcmService.class.getName();
	}
}
