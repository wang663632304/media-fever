package com.mediafever.android;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.jdroid.android.tabs.TabAction;
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
public enum HomeItem implements TabAction {
	
	WATCHED(R.id.watched, R.drawable.watched_action_selector, R.string.watched, R.string.watchedDescription,
			WatchedListActivity.class),
	WISHLIST(R.id.wishList, R.drawable.wishlist_action_selector, R.string.wishList, R.string.wishListDescription,
			WishListActivity.class),
	MEDIA_SESSIONS(R.id.mediaSessions, R.drawable.media_sessions_action_selector, R.string.mediaSessions,
			R.string.mediaSessionsDescription, MediaSessionListActivity.class),
	WATCH_TO_WATCH(R.id.whatToWatch, R.drawable.what_to_watch_action_selector, R.string.whatToWatch,
			R.string.whatToWatchDescription, WhatToWatchActivity.class),
	FRIENDS(R.id.friends, R.drawable.friends_action_selector, R.string.friends, R.string.friendsDescription,
			FriendsActivity.class);
	
	private int viewId;
	private int iconResource;
	private int nameResource;
	private int descriptionResource;
	private Class<? extends Activity> targetActivity;
	
	private HomeItem(int viewId, int iconResource, int nameResource, int descriptionResource,
			Class<? extends Activity> targetActivity) {
		this.viewId = viewId;
		this.iconResource = iconResource;
		this.nameResource = nameResource;
		this.descriptionResource = descriptionResource;
		this.targetActivity = targetActivity;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getIconResource()
	 */
	@Override
	public int getIconResource() {
		return iconResource;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getNameResource()
	 */
	@Override
	public int getNameResource() {
		return nameResource;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getActivityClass()
	 */
	@Override
	public Class<? extends Activity> getActivityClass() {
		return targetActivity;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getName()
	 */
	@Override
	public String getName() {
		return name();
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#createFragment(java.lang.Object)
	 */
	@Override
	public Fragment createFragment(Object args) {
		return null;
	}
	
	/**
	 * @return the descriptionResource
	 */
	public int getDescriptionResource() {
		return descriptionResource;
	}
	
	/**
	 * @return the viewId
	 */
	public int getViewId() {
		return viewId;
	}
	
}