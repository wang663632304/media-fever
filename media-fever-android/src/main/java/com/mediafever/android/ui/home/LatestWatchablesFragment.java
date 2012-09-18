package com.mediafever.android.ui.home;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.jdroid.android.coverflow.CoverFlow;
import com.jdroid.android.coverflow.CoverFlowImageAdapter;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.fragment.AbstractFragment;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.usecase.LatestWatchablesUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class LatestWatchablesFragment extends AbstractFragment {
	
	private LatestWatchablesUseCase latestWatchablesUseCase;
	
	@InjectView(R.id.coverflow)
	private CoverFlow coverflow;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.latest_watchables_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (latestWatchablesUseCase == null) {
			latestWatchablesUseCase = getInstance(LatestWatchablesUseCase.class);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(latestWatchablesUseCase, this, true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(latestWatchablesUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				
				coverflow.setAdapter(new CoverFlowImageAdapter<Watchable>(getActivity(),
						latestWatchablesUseCase.getWatchables(), getResources().getDimensionPixelSize(
							R.dimen.coverFlowWidth), getResources().getDimensionPixelSize(R.dimen.coverFlowHeight)) {
					
					@Override
					protected FileContent getFileContent(Watchable item) {
						return item.getImage();
					}
					
					@Override
					protected int getDefaultDrawableId() {
						return R.drawable.watchable_coverflow_default;
					}
				});
				
				coverflow.setSelection(latestWatchablesUseCase.getWatchables().size() / 2, true);
				setupListeners(coverflow);
				
				dismissLoading();
			}
		});
	}
	
	/**
	 * Sets the up listeners.
	 * 
	 * @param coverFlow the new up listeners
	 */
	private void setupListeners(CoverFlow coverFlow) {
		coverFlow.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				Watchable watchable = (Watchable)parent.getAdapter().getItem(position);
				WatchableAdapter.onItemClick(LatestWatchablesFragment.this.getActivity(), watchable);
			}
			
		});
	}
}
