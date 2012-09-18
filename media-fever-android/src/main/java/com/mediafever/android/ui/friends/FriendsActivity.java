package com.mediafever.android.ui.friends;

import java.util.List;
import com.jdroid.android.contextual.ContextualActivity;
import com.jdroid.java.collections.Lists;

/**
 * Friends Activity
 * 
 * @author Maxi Rosson
 */
public class FriendsActivity extends ContextualActivity<FriendsContextualItem> {
	
	/**
	 * @see com.jdroid.android.contextual.ContextualActivity#getContextualItems()
	 */
	@Override
	protected List<FriendsContextualItem> getContextualItems() {
		return Lists.newArrayList(FriendsContextualItem.values());
	}
	
	/**
	 * @see com.jdroid.android.contextual.ContextualActivity#getDefaultContextualItem()
	 */
	@Override
	protected FriendsContextualItem getDefaultContextualItem() {
		return FriendsContextualItem.MY_FRIENDS;
	}
}
