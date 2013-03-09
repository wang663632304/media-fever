package com.mediafever.android.ui.watchable.whattowatch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.usecase.SuggestionsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SuggestionsListFragment extends AbstractListFragment<Watchable> {
	
	private SuggestionsUseCase suggestionsUseCase;
	
	/**
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		suggestionsUseCase = getInstance(SuggestionsUseCase.class);
		suggestionsUseCase.setUserId(getUser().getId());
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(suggestionsUseCase, this, UseCaseTrigger.ONCE);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(suggestionsUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(new WatchableAdapter(getActivity(), suggestionsUseCase.getWatchables()));
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
