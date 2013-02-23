package com.mediafever.android.ui.session;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsActivity extends FragmentContainerActivity {
	
	public static void start(Context context, MediaSession mediaSession) {
		Intent intent = new Intent(context, MediaSelectionsActivity.class);
		intent.putExtra(MediaSelectionsFragment.MEDIA_SESSION_EXTRA, mediaSession);
		context.startActivity(intent);
	}
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#createNewFragment()
	 */
	@Override
	protected Fragment createNewFragment() {
		return MediaSelectionsFragment.instance(getIntent().getExtras());
	}
}
