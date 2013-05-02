package com.mediafever.android.ui.session;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsActivity extends FragmentContainerActivity {
	
	public static void start(Context context, Long mediaSessionId, Boolean mediaSessionCreated) {
		Intent intent = new Intent(context, MediaSelectionsActivity.class);
		intent.putExtra(MediaSelectionsFragment.MEDIA_SESSION_ID_EXTRA, mediaSessionId);
		intent.putExtra(MediaSelectionsFragment.MEDIA_SESSION_CREATED_EXTRA, mediaSessionCreated);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);
	}
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return MediaSelectionsFragment.class;
	}
	
	/**
	 * @see android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Fragment fragment = instanceFragment(getFragmentClass(), intent.getExtras());
		replaceFragment(fragment);
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		int menuResourceId = 0;
		if (AndroidUtils.isGoogleTV()) {
			menuResourceId = R.menu.media_session_google_tv_menu;
		} else {
			menuResourceId = R.menu.media_session_menu;
		}
		return menuResourceId;
	}
}
