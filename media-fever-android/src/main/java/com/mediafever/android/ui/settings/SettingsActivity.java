package com.mediafever.android.ui.settings;

import java.util.List;
import android.support.v4.app.Fragment;
import com.jdroid.android.contextual.ContextualActivity;
import com.jdroid.android.contextual.ContextualListFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Lists;

/**
 * Settings Activity
 * 
 * @author Maxi Rosson
 */
public class SettingsActivity extends ContextualActivity<SettingsContextualItem> {
	
	/**
	 * @see com.jdroid.android.contextual.ContextualActivity#getContextualItems()
	 */
	@Override
	protected List<SettingsContextualItem> getContextualItems() {
		return Lists.newArrayList(SettingsContextualItem.values());
	}
	
	/**
	 * @see com.jdroid.android.contextual.ContextualActivity#getDefaultContextualItem()
	 */
	@Override
	protected SettingsContextualItem getDefaultContextualItem() {
		return SettingsContextualItem.PROFILE;
	}
	
	@Override
	protected Fragment newContextualListFragment(List<SettingsContextualItem> actions,
			SettingsContextualItem defaultContextualItem) {
		return AndroidUtils.isLargeScreenOrBigger() ? new SettingsLargeContextualListFragment(getContextualItems(),
				defaultContextualItem) : new ContextualListFragment(getContextualItems(), defaultContextualItem);
	}
}
