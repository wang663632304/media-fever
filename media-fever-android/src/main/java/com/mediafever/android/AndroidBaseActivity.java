package com.mediafever.android;

import android.annotation.TargetApi;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.activity.BaseActivity;
import com.jdroid.android.leftnavbar.LeftNavBar;
import com.jdroid.android.share.ShareIntent;
import com.jdroid.android.tabs.TabAction;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.android.ui.AboutDialogFragment;
import com.mediafever.android.ui.home.HomeActivity;
import com.mediafever.android.ui.listener.BuyFullAppOnClickListener;
import com.mediafever.android.ui.settings.SettingsActivity;
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
			if (isLeftNavBarEnabled()) {
				actionBar.setDisplayShowHomeEnabled(false);
			} else {
				actionBar.setDisplayUseLogoEnabled(false);
			}
		}
	}
	
	private Boolean isLeftNavBarEnabled() {
		return AndroidUtils.isGoogleTV();
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (isLeftNavBarEnabled() && getActivityIf().requiresAuthentication()
				&& !getActivity().getClass().equals(HomeActivity.class)) {
			loadLeftNavBar();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadLeftNavBar() {
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
		leftNavBar.flipOption(LeftNavBar.DISPLAY_AUTO_EXPAND);
		leftNavBar.flipOption(LeftNavBar.DISPLAY_USE_LOGO_WHEN_EXPANDED);
		
		for (HomeItem leftAction : HomeItem.values()) {
			addLeftNavBarTab(leftAction);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void addLeftNavBarTab(final TabAction leftAction) {
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
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		int menuResourceId = 0;
		if (getActivityIf().requiresAuthentication()) {
			if (AndroidUtils.isGoogleTV()) {
				menuResourceId = R.menu.google_tv_menu;
			} else {
				menuResourceId = R.menu.menu;
			}
		} else {
			menuResourceId = R.menu.not_authenticated_menu;
		}
		return menuResourceId;
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#doOnCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void doOnCreateOptionsMenu(Menu menu) {
		super.doOnCreateOptionsMenu(menu);
		if (getActivityIf().requiresAuthentication() && AndroidUtils.isGoogleTV()) {
			SearchView searchView = (SearchView)menu.findItem(R.id.searchItem).getActionView();
			searchView.setFocusable(true);
			searchView.setSubmitButtonEnabled(true);
			
			// Get the SearchView and set the searchable configuration
			SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
			searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
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
			case R.id.shareItem:
				String appName = AbstractApplication.get().getAppName();
				String shareEmailSubject = getActivity().getString(R.string.shareSubject);
				String shareEmailContent = getActivity().getString(R.string.shareContent, AndroidUtils.getPackageName());
				
				ShareIntent.share(appName, shareEmailSubject, shareEmailContent);
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
