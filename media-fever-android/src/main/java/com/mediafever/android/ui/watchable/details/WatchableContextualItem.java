package com.mediafever.android.ui.watchable.details;

import java.util.List;
import android.app.Activity;
import android.support.v4.app.Fragment;
import com.jdroid.android.tabs.TabAction;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Lists;
import com.mediafever.R;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Series;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public enum WatchableContextualItem implements TabAction {
	
	OVERVIEW(R.string.overview) {
		
		@SuppressWarnings("unchecked")
		@Override
		public Fragment createFragment(Object args) {
			return AndroidUtils.isLargeScreenOrBigger() ? new WatchableOverviewLargeFragment(
					(UserWatchable<Watchable>)args) : new WatchableOverviewFragment((UserWatchable<Watchable>)args);
		}
	},
	SOCIAL(R.string.social) {
		
		@Override
		public Fragment createFragment(Object args) {
			return new WatchableSocialFragment();
		}
	},
	SEASONS(R.string.seasons) {
		
		@SuppressWarnings("unchecked")
		@Override
		public Fragment createFragment(Object args) {
			return new SeriesSeasonsFragment((UserWatchable<Series>)args);
		}
	};
	
	private Integer resourceId;
	
	private WatchableContextualItem(Integer resourceId) {
		this.resourceId = resourceId;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#createFragment(java.lang.Object)
	 */
	@Override
	public abstract Fragment createFragment(Object args);
	
	private static List<WatchableContextualItem> getMoviesContextualItems() {
		return Lists.newArrayList(WatchableContextualItem.OVERVIEW, WatchableContextualItem.SOCIAL);
	}
	
	private static List<WatchableContextualItem> getSeriesContextualItems(UserWatchable<Watchable> userWatchable) {
		List<WatchableContextualItem> items = Lists.newArrayList(WatchableContextualItem.OVERVIEW,
			WatchableContextualItem.SOCIAL);
		Series series = (Series)userWatchable.getWatchable();
		if (series.hasSeasons()) {
			items.add(WatchableContextualItem.SEASONS);
		}
		return items;
	}
	
	public static List<WatchableContextualItem> getWatchableContextualItems(UserWatchable<Watchable> userWatchable) {
		WatchableType watchableType = WatchableType.find(userWatchable.getWatchable());
		if (watchableType.equals(WatchableType.MOVIE)) {
			return WatchableContextualItem.getMoviesContextualItems();
		} else if (watchableType.equals(WatchableType.SERIES)) {
			return WatchableContextualItem.getSeriesContextualItems(userWatchable);
		} else {
			return Lists.newArrayList();
		}
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
		return 0;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getActivityClass()
	 */
	@Override
	public Class<? extends Activity> getActivityClass() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.tabs.TabAction#getName()
	 */
	@Override
	public String getName() {
		return name();
	}
}
