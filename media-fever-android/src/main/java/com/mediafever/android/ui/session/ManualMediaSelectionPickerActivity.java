package com.mediafever.android.ui.session;

import java.io.Serializable;
import java.util.List;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class ManualMediaSelectionPickerActivity extends FragmentContainerActivity {
	
	public static void start(Fragment fragment, Long mediaSessionId, List<WatchableType> watchableTypes) {
		Intent intent = new Intent(fragment.getActivity(), ManualMediaSelectionPickerActivity.class);
		intent.putExtra(ManualMediaSelectionPickerFragment.MEDIA_SESSION_ID_EXTRA, mediaSessionId);
		intent.putExtra(ManualMediaSelectionPickerFragment.WATCHABLES_TYPES_EXTRA, (Serializable)watchableTypes);
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
