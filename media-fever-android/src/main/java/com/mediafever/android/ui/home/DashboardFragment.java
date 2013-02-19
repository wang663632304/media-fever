package com.mediafever.android.ui.home;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.listener.LaunchOnClickListener;
import com.mediafever.R;
import com.mediafever.android.ui.friends.FriendsActivity;
import com.mediafever.android.ui.session.MediaSessionListActivity;
import com.mediafever.android.ui.watchable.watched.WatchedListActivity;
import com.mediafever.android.ui.watchable.whattowatch.WhatToWatchActivity;
import com.mediafever.android.ui.watchable.wishlist.WishListActivity;

/**
 * 
 * @author Maxi Rosson
 */
public class DashboardFragment extends AbstractFragment {
	
	@InjectView(R.id.friends)
	private View friends;
	
	@InjectView(R.id.wishList)
	private View wishList;
	
	@InjectView(R.id.watched)
	private View watched;
	
	@InjectView(R.id.mediaSessions)
	private View mediaSessions;
	
	@InjectView(R.id.whatToWatch)
	private View whatToWatch;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
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
		
		friends.setOnClickListener(new LaunchOnClickListener(FriendsActivity.class));
		wishList.setOnClickListener(new LaunchOnClickListener(WishListActivity.class));
		watched.setOnClickListener(new LaunchOnClickListener(WatchedListActivity.class));
		mediaSessions.setOnClickListener(new LaunchOnClickListener(MediaSessionListActivity.class));
		whatToWatch.setOnClickListener(new LaunchOnClickListener(WhatToWatchActivity.class));
	}
}
