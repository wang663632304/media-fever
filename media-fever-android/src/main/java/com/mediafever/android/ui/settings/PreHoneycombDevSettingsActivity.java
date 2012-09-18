package com.mediafever.android.ui.settings;

import android.os.Bundle;
import com.jdroid.android.activity.AbstractPreferenceActivity;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public class PreHoneycombDevSettingsActivity extends AbstractPreferenceActivity {
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.dev_preferences);
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractPreferenceActivity#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return false;
	}
}
