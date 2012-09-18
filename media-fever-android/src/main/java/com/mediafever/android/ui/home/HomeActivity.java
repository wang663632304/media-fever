package com.mediafever.android.ui.home;

import android.os.Bundle;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.android.ui.login.LoginActivity;

/**
 * 
 * @author Maxi Rosson
 */
public class HomeActivity extends AbstractFragmentActivity {
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return R.layout.home_activity;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onBeforeSetContentView()
	 */
	@Override
	public Boolean onBeforeSetContentView() {
		if (isAuthenticated()) {
			return true;
		} else {
			ActivityLauncher.launchActivity(LoginActivity.class);
			finish();
			return false;
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			commitFragment(R.id.latestFragmentContainer, new LatestWatchablesFragment());
			if (!AndroidUtils.isLargeScreenOrBigger()) {
				commitFragment(R.id.dashboardFragmentContainer, new DashboardFragment());
			}
		}
	};
	
}
