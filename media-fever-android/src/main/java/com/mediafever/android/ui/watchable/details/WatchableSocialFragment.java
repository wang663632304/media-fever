package com.mediafever.android.ui.watchable.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractFragment;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableSocialFragment extends AbstractFragment {
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Implment this
		return inflater.inflate(R.layout.social_settings_fragment, container, false);
	}
	
}