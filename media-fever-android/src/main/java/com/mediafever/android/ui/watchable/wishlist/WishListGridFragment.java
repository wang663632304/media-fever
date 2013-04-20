package com.mediafever.android.ui.watchable.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.utils.AnimationUtils;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.usecase.WishListUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class WishListGridFragment extends AbstractGridFragment<Watchable> {
	
	private WishListUseCase wishListUseCase;
	
	/**
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		wishListUseCase = getInstance(WishListUseCase.class);
		wishListUseCase.setUserId(getUser().getId());
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.grid_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#getNoResultsText()
	 */
	@Override
	protected int getNoResultsText() {
		return R.string.noResultsWishList;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(wishListUseCase, this, UseCaseTrigger.ONCE);
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
				setListAdapter(new WatchableAdapter(getActivity(), wishListUseCase.getWatchables()));
				AnimationUtils.makeViewGroupAnimation(getGridView());
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(Watchable watchable) {
		WatchableActivity.start(getActivity(), watchable);
	}
}
