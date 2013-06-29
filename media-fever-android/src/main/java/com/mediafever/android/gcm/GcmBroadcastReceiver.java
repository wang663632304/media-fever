package com.mediafever.android.gcm;

import android.content.Context;
import com.jdroid.android.gcm.AbstractGcmBroadcastReceiver;

/**
 * 
 * @author Maxi Rosson
 */
public class GcmBroadcastReceiver extends AbstractGcmBroadcastReceiver {
	
	/**
	 * @see com.google.android.gcm.GCMBroadcastReceiver#getGCMIntentServiceClassName(android.content.Context)
	 */
	@Override
	protected String getGCMIntentServiceClassName(Context context) {
		return GcmService.class.getName();
	}
}
