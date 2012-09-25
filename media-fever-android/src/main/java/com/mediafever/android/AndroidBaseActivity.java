package com.mediafever.android;

import android.annotation.TargetApi;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.SearchView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.activity.BaseActivity;
import com.jdroid.android.leftnavbar.LeftNavBar;
import com.jdroid.android.tabs.TabAction;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.android.ui.AboutDialogFragment;
import com.mediafever.android.ui.friends.FriendsActivity;
import com.mediafever.android.ui.listener.BuyFullAppOnClickListener;
import com.mediafever.android.ui.session.MediaSessionListActivity;
import com.mediafever.android.ui.settings.DevSettingsActivity;
import com.mediafever.android.ui.settings.PreHoneycombDevSettingsActivity;
import com.mediafever.android.ui.settings.SettingsActivity;
import com.mediafever.android.ui.watchable.watched.WatchedListActivity;
import com.mediafever.android.ui.watchable.whattowatch.WhatToWatchActivity;
import com.mediafever.android.ui.watchable.wishlist.WishListActivity;
import com.mediafever.context.ApplicationContext;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidBaseActivity extends BaseActivity {
	
	private LeftNavBar leftNavBar;
	
	public AndroidBaseActivity(Activity activity) {
		super(activity);
		
		ActionBar actionBar = getActivityIf().getSupportActionBar();
		if (actionBar != null) {
			if (AndroidUtils.isLargeScreenOrBigger()) {
				actionBar.setDisplayShowHomeEnabled(false);
			} else {
				actionBar.setDisplayUseLogoEnabled(false);
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (AndroidUtils.isLargeScreenOrBigger() && getActivityIf().requiresAuthentication()) {
			
			leftNavBar = new LeftNavBar(getActivity());
			leftNavBar.setOnClickHomeListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ActivityLauncher.launchHomeActivity();
				}
			});
			leftNavBar.setBackgroundDrawable(getActivity().getResources().getDrawable(
				R.drawable.leftnav_bar_background_dark));
			if (ApplicationContext.get().isFreeApp()) {
				View customView = inflate(R.layout.leftnavbar_custom_view);
				View buyFullApp = customView.findViewById(R.id.buyFullApp);
				buyFullApp.setOnClickListener(new BuyFullAppOnClickListener());
				leftNavBar.setDisplayShowCustomEnabled(true);
				leftNavBar.setCustomView(customView);
			}
			leftNavBar.setTitle(getActivity().getTitle());
			leftNavBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			leftNavBar.flipOption(ActionBar.DISPLAY_SHOW_TITLE);
			if (AndroidUtils.isGoogleTV()) {
				leftNavBar.flipOption(LeftNavBar.DISPLAY_AUTO_EXPAND);
			}
			leftNavBar.flipOption(LeftNavBar.DISPLAY_USE_LOGO_WHEN_EXPANDED);
			
			for (LeftAction leftAction : LeftAction.values()) {
				addTab(leftAction);
			}
			
		}
	}
	
	@TargetApi(11)
	private void addTab(final TabAction leftAction) {
		Tab tab = leftNavBar.newTab();
		tab.setText(leftAction.getNameResource());
		tab.setIcon(leftAction.getIconResource());
		tab.setTabListener(new TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// Do Nothing
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				ActivityLauncher.launchActivity(leftAction.getActivityClass());
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				leftNavBar.collapse();
			}
		});
		leftNavBar.addTab(tab, leftAction.getActivityClass().equals(getActivity().getClass()));
	}
	
	public enum LeftAction implements TabAction {
		
		// TODO Replace the friends action selector once we have all the action's images
		WATCHED(R.drawable.friends_action_selector, R.string.watched, WatchedListActivity.class),
		WISHLIST(R.drawable.friends_action_selector, R.string.wishList, WishListActivity.class),
		MEDIA_SESSIONS(R.drawable.friends_action_selector, R.string.mediaSessions, MediaSessionListActivity.class),
		WATCH_TO_WATCH(R.drawable.friends_action_selector, R.string.whatToWatch, WhatToWatchActivity.class),
		FRIENDS(R.drawable.friends_action_selector, R.string.friends, FriendsActivity.class);
		
		private int iconResource;
		private int nameResource;
		private Class<? extends Activity> targetActivity;
		
		private LeftAction(int iconResource, int nameResource, Class<? extends Activity> targetActivity) {
			this.iconResource = iconResource;
			this.nameResource = nameResource;
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
		
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		int menuResourceId = 0;
		if (getActivityIf().requiresAuthentication()) {
			if (AndroidUtils.isGoogleTV()) {
				menuResourceId = R.menu.google_tv_menu;
			} else if (AndroidUtils.isLargeScreenOrBigger()) {
				menuResourceId = R.menu.tablet_menu;
			} else {
				menuResourceId = R.menu.handset_menu;
			}
		} else {
			if (AndroidUtils.isLargeScreenOrBigger()) {
				menuResourceId = R.menu.not_authenticated_menu;
			} else {
				menuResourceId = R.menu.not_authenticated_handset_menu;
			}
		}
		return menuResourceId;
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#doOnCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	@TargetApi(11)
	public void doOnCreateOptionsMenu(Menu menu) {
		if (getActivityIf().requiresAuthentication() && AndroidUtils.isLargeScreenOrBigger()) {
			SearchView searchView = (SearchView)menu.findItem(R.id.searchItem).getActionView();
			searchView.setFocusable(true);
			searchView.setSubmitButtonEnabled(true);
			
			// Get the SearchView and set the searchable configuration
			SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
			searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
		}
		
		if (!ApplicationContext.get().displayDevSettings()) {
			MenuItem menuItem = menu.findItem(R.id.devSettingsItem);
			if (menuItem != null) {
				menuItem.setVisible(false);
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.searchItem:
				getActivity().onSearchRequested();
				return true;
			case R.id.aboutItem:
				new AboutDialogFragment().show(getActivity());
				return true;
			case R.id.settingsItem:
				ActivityLauncher.launchActivity(SettingsActivity.class);
				return true;
			case R.id.logoutItem:
				AndroidApplication.get().logout();
				return true;
			case R.id.devSettingsItem:
				
				Class<? extends Activity> targetActivity;
				if (AndroidUtils.getApiLevel() < 11) {
					targetActivity = PreHoneycombDevSettingsActivity.class;
				} else {
					targetActivity = DevSettingsActivity.class;
				}
				ActivityLauncher.launchActivity(targetActivity);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
