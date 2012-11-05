package com.mediafever.android.ui.settings;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialSettingsActivity extends FragmentContainerActivity {
	
	Fragment fragment;
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		fragment = new SocialSettingsFragment();
		return fragment;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		fragment.onActivityResult(requestCode, resultCode, data);
	}
}
