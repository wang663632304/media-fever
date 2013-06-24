package com.mediafever.android.gcm;

import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class GcmMessageBroadcastReceiver extends BroadcastReceiver {
	
	private List<GcmMessage> messagesToListen;
	
	public GcmMessageBroadcastReceiver(List<GcmMessage> messagesToListen) {
		this.messagesToListen = messagesToListen;
	}
	
	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		GcmMessage gcmMessage = GcmMessage.find(intent);
		if (messagesToListen.contains(gcmMessage)) {
			onGcmMessage(gcmMessage, intent);
		}
	}
	
	protected abstract void onGcmMessage(GcmMessage gcmMessage, Intent intent);
}
