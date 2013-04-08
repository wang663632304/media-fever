package com.mediafever.android.ui.session;

import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;

/**
 * 
 * @author Maxi Rosson
 */
public class ManualMediaSelectionPickerActivity extends FragmentContainerActivity {
	
	public static void start(Fragment fragment, Long mediaSessionId) {
		Intent intent = new Intent(fragment.getActivity(), ManualMediaSelectionPickerActivity.class);
		intent.putExtra(ManualMediaSelectionPickerFragment.MEDIA_SESSION_ID_EXTRA, mediaSessionId);
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
