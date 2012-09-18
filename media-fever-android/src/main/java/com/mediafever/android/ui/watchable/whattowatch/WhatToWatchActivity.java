package com.mediafever.android.ui.watchable.whattowatch;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.utils.AndroidUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class WhatToWatchActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		return AndroidUtils.isLargeScreenOrBigger() ? new SuggestionsGridFragment() : new SuggestionsListFragment();
	}
}