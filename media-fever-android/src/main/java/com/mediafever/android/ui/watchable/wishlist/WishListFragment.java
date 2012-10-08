package com.mediafever.android.ui.watchable.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractListFragment;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.usecase.WishListUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class WishListFragment extends AbstractListFragment<Watchable> {
	
	private WishListUseCase wishListUseCase;
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#getNoResultsText()
	 */
	@Override
	protected int getNoResultsText() {
		return R.string.noResultsWishList;
	}
	
	/**
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		if (wishListUseCase == null) {
			wishListUseCase = getInstance(WishListUseCase.class);
			wishListUseCase.setUserId(getUser().getId());
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(wishListUseCase, this, true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(wishListUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(new WatchableAdapter(WishListFragment.this.getActivity(),
						wishListUseCase.getWatchables()));
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(Watchable watchable) {
		WatchableAdapter.onItemClick(this.getActivity(), watchable);
	}
}