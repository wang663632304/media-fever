package com.mediafever.android.ui.watchable.watched;

import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.utils.AndroidUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchedListActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		return AndroidUtils.isLargeScreenOrBigger() ? new WatchedGridFragment() : new WatchedListFragment();
	}
}