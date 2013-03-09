package com.mediafever.android.ui.session;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsActivity extends FragmentContainerActivity {
	
	public static void start(Context context, MediaSession mediaSession, Boolean mediaSessionCreated) {
		Intent intent = new Intent(context, MediaSelectionsActivity.class);
		intent.putExtra(MediaSelectionsFragment.MEDIA_SESSION_EXTRA, mediaSession);
		intent.putExtra(MediaSelectionsFragment.MEDIA_SESSION_CREATED_EXTRA, mediaSessionCreated);
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
