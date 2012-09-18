package com.mediafever.android.ui.settings;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.jdroid.android.tabs.TabAction;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public enum SettingsContextualItem implements TabAction {
	
	PROFILE(R.string.profile, R.drawable.friends_contextual_selector, ProfileActivity.class) {
		
		@Override
		public Fragment createFragment(Object args) {
			return new ProfileFragment();
		}
	},
	SOCIAL(R.string.social, R.drawable.friends_contextual_selector, SocialSettingsActivity.class) {
		
		@Override
		public Fragment createFragment(Object args) {
			return new SocialSettingsFragment();
		}
	};
	
	private Integer resourceId;
	private Integer iconId;
	private Class<? extends Activity> activityClass;
	
	private SettingsContextualItem(Integer resourceId, Integer iconId, Class<? extends Activity> activityClass) {
		this.resourceId = resourceId;
		this.iconId = iconId;
		this.activityClass = activityClass;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getNameResource()
	 */
	@Override
	public int getNameResource() {
		return resourceId;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getIconResource()
	 */
	@Override
	public int getIconResource() {
		return iconId;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getActivityClass()
	 */
	@Override
	public Class<? extends Activity> getActivityClass() {
		return activityClass;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getName()
	 */
	@Override
	public String getName() {
		return name();
	}
}
