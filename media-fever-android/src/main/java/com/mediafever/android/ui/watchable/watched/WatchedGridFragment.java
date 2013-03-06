package com.mediafever.android.ui.watchable.watched;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.utils.AnimationUtils;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.usecase.WatchedUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchedGridFragment extends AbstractGridFragment<Watchable> {
	
	private WatchedUseCase watchedUseCase;
	
	/**
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		watchedUseCase = getInstance(WatchedUseCase.class);
		watchedUseCase.setUserId(getUser().getId());
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
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(watchedUseCase, this, UseCaseTrigger.ONCE);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(watchedUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(new WatchableAdapter(getActivity(), watchedUseCase.getWatchables()));
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
