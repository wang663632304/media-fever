package com.mediafever.android.ui.session;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class ManualMediaSelectionPickerActivity extends FragmentContainerActivity {
	
	public static void start(Fragment fragment, MediaSession mediaSession) {
		Intent intent = new Intent(fragment.getActivity(), ManualMediaSelectionPickerActivity.class);
		intent.putExtra(MediaSelectionsFragment.MEDIA_SESSION_EXTRA, mediaSession);
		fragment.startActivityForResult(intent, MediaSelectionsFragment.MEDIA_SELECTION_ADDED_REQUEST_CODE);
	}
	
	/**
	 * @see com.jdroid.android.activity.FragmentContainerActivity#getFragmentClass()
	 */
	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ManualMediaSelectionPickerFragment.class;
	}
}
