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
		
		// TODO Make it work
		// View header = inflate(R.layout.device_info_header);
		// ((TextView)header.findViewById(R.id.screenSize)).setText("Screen Size: " + AndroidUtils.getScreenSize());
		// ((TextView)header.findViewById(R.id.screenDensity)).setText("Screen Density: "
		// + AndroidUtils.getScreenDensity());
		//
		// getListView().addFooterView(header);
	}
}
