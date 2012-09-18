package com.mediafever.android.ui.settings;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;

/**
 * Settings Activity just for development purposes. These settings are disabled on the production environment
 * 
 * @author Maxi Rosson
 */
public class DevSettingsActivity extends AbstractFragmentActivity {
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return R.layout.fragment_container_activity;
	}
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.fragmentContainer, new DevSettingsFragment());
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractPreferenceActivity#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return false;
	}
}