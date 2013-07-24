package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookFriendsActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			addFragment(new FacebookFriendsHelperFragment(), 0, false);
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		return new FacebookFriendsListFragment();
	}
}