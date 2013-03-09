package com.mediafever.android.ui.home;

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
		
		findView(R.id.friends).setOnClickListener(new LaunchOnClickListener(FriendsActivity.class));
		findView(R.id.wishList).setOnClickListener(new LaunchOnClickListener(WishListActivity.class));
		findView(R.id.watched).setOnClickListener(new LaunchOnClickListener(WatchedListActivity.class));
		findView(R.id.mediaSessions).setOnClickListener(new LaunchOnClickListener(MediaSessionListActivity.class));
		findView(R.id.whatToWatch).setOnClickListener(new LaunchOnClickListener(WhatToWatchActivity.class));
	}
}
