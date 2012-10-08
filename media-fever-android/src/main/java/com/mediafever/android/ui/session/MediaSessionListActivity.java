package com.mediafever.android.ui.session;

import android.support.v4.app.Fragment;
import com.actionbarsherlock.view.MenuItem;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionListActivity extends FragmentContainerActivity {
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		return new MediaSessionsFragment();
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		int menuResourceId = 0;
		if (AndroidUtils.isGoogleTV()) {
			menuResourceId = R.menu.media_sessions_google_tv_menu;
		} else if (AndroidUtils.isLargeScreenOrBigger()) {
			menuResourceId = R.menu.media_sessions_tablet_menu;
		} else {
			menuResourceId = R.menu.media_sessions_handset_menu;
		}
		return menuResourceId;
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.addMediaSessionItem:
				ActivityLauncher.launchActivity(MediaSessionActivity.class);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}