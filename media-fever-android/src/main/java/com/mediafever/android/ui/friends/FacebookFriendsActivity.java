package com.mediafever.android.ui.friends;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookFriendsActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		return new FacebookFriendsListFragment();
	}
}