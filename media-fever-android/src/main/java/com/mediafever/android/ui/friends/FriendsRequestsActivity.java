package com.mediafever.android.ui.friends;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendsRequestsActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		return new FriendsRequestsFragment();
	}
	
	/**
	 * @see android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		replaceFragment(createNewFragment());
	}
}
