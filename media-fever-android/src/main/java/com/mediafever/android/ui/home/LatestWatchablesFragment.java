package com.mediafever.android.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.coverflow.CoverFlow;
import com.jdroid.android.coverflow.CoverFlowImageAdapter;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.images.ReflectedRemoteImageResolver;
import com.jdroid.java.exception.ConnectionException;
import com.jdroid.java.utils.CollectionUtils;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.usecase.LatestWatchablesUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class LatestWatchablesFragment extends AbstractFragment {
	
	private static final String TAG = LatestWatchablesFragment.class.getSimpleName();
	
	private LatestWatchablesUseCase latestWatchablesUseCase;
	
	private CoverFlow coverflow;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		latestWatchablesUseCase = getInstance(LatestWatchablesUseCase.class);
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
		
		float height = getResources().getDimensionPixelSize(R.dimen.coverFlowHeight)
				* (1 + ReflectedRemoteImageResolver.get().getImageReflectionRatio());
		
		coverflow = findView(R.id.coverflow);
		coverflow.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)height));
		loadCoverflow();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(latestWatchablesUseCase, this, UseCaseTrigger.ONCE);
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
	 * @see com.jdroid.android.fragment.AbstractFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do nothing
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		Log.e(TAG, "Failed to execute the use case", runtimeException);
		if (runtimeException instanceof ConnectionException) {
			executeUseCase(latestWatchablesUseCase, 10L);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				loadCoverflow();
			}
		});
	}
	
	private void loadCoverflow() {
		if (CollectionUtils.isNotEmpty(latestWatchablesUseCase.getWatchables())) {
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
			coverflow.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
					Watchable watchable = (Watchable)parent.getAdapter().getItem(position);
					WatchableActivity.start(getActivity(), watchable);
				}
				
			});
		}
		
	}
}
