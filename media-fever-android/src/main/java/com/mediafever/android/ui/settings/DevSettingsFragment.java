package com.mediafever.android.ui.settings;

import android.os.Bundle;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public class DevSettingsFragment extends AbstractPreferenceFragment {
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.dev_preferences);
	}
}
