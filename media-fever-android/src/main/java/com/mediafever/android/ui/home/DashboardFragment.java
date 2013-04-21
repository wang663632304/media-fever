package com.mediafever.android.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.listener.LaunchOnClickListener;
import com.mediafever.R;
import com.mediafever.android.HomeItem;

/**
 * 
 * @author Maxi Rosson
 */
public class DashboardFragment extends AbstractFragment {
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dashboard_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		for (HomeItem homeItem : HomeItem.values()) {
			View item = findView(homeItem.getViewId());
			item.setOnClickListener(new LaunchOnClickListener(homeItem.getActivityClass()));
			((ImageView)item.findViewById(R.id.dashboardIcon)).setImageResource(homeItem.getIconResource());
			((TextView)item.findViewById(R.id.dashboardText)).setText(homeItem.getNameResource());
			((TextView)item.findViewById(R.id.dashboardTextDescription)).setText(homeItem.getDescriptionResource());
		}
	}
}
