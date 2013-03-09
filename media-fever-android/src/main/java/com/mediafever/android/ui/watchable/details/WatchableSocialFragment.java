package com.mediafever.android.ui.watchable.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.commonsware.cwac.merge.MergeAdapter;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.view.ListSeparatorView;
import com.jdroid.java.utils.CollectionUtils;
import com.mediafever.R;
import com.mediafever.android.ui.UserAdapter;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableSocialFragment extends AbstractListFragment<User> {
	
	private static final String USER_WATCHABLE_EXTRA = "userWatchable";
	
	private UserWatchable<Watchable> userWatchable;
	
	public static WatchableSocialFragment instance(UserWatchable<Watchable> userWatchable) {
		WatchableSocialFragment fragment = new WatchableSocialFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(USER_WATCHABLE_EXTRA, userWatchable);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		userWatchable = getArgument(USER_WATCHABLE_EXTRA);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		MergeAdapter mergeAdapter = new MergeAdapter();
		
		if (CollectionUtils.isNotEmpty(userWatchable.getWatchedBy())) {
			int resId = userWatchable.getWatchedBy().size() == 1 ? R.string.watchedByFriend : R.string.watchedByFriends;
			mergeAdapter.addView(new ListSeparatorView(getActivity(), getString(resId,
				userWatchable.getWatchedBy().size())));
			mergeAdapter.addAdapter(new UserAdapter(getActivity(), userWatchable.getWatchedBy(),
					R.layout.user_small_item, false));
		}
		
		if (CollectionUtils.isNotEmpty(userWatchable.getOnTheWishListOf())) {
			int resId = userWatchable.getOnTheWishListOf().size() == 1 ? R.string.onTheWishListOfFriend
					: R.string.onTheWishListOfFriends;
			mergeAdapter.addView(new ListSeparatorView(getActivity(), getString(resId,
				userWatchable.getOnTheWishListOf().size())));
			mergeAdapter.addAdapter(new UserAdapter(getActivity(), userWatchable.getOnTheWishListOf(),
					R.layout.user_small_item, false));
		}
		setListAdapter(mergeAdapter);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#getNoResultsText()
	 */
	@Override
	protected int getNoResultsText() {
		return WatchableType.MOVIE.match(userWatchable.getWatchable()) ? R.string.noResultsMovieSocial
				: R.string.noResultsSeriesSocial;
	}
}