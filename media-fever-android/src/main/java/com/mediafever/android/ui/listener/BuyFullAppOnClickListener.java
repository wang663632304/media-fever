package com.mediafever.android.ui.listener;

import android.view.View;
import android.view.View.OnClickListener;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.GooglePlayUtils;
import com.mediafever.android.AndroidApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class BuyFullAppOnClickListener implements OnClickListener {
	
	@Override
	public void onClick(View v) {
		GooglePlayUtils.launchAppDetails(AndroidApplication.get().getCurrentActivity(),
			AndroidUtils.getPackageName().replace(".lite", ""));
	}
	
}
