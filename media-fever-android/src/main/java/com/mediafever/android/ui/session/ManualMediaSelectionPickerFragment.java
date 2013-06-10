package com.mediafever.android.ui.session;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.AndroidUseCaseListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.fragment.AbstractSearchFragment;
import com.jdroid.android.usecase.SearchUseCase;
import com.jdroid.java.collections.Lists;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.usecase.WatchablesSuggestionsUseCase;
import com.mediafever.usecase.mediasession.AddManualSelectionUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class ManualMediaSelectionPickerFragment extends AbstractSearchFragment<Watchable> {
	
	public static final String MEDIA_SESSION_ID_EXTRA = "mediaSessionId";
	public static final String WATCHABLES_TYPES_EXTRA = "watchableTypes";
	
	private WatchablesSuggestionsUseCase watchablesSuggestionsUseCase;
	private AddManualSelectionUseCase addManualSelectionUseCase;
	private AndroidUseCaseListener addManualSelectionUseCaseListener;
	
	private Long mediaSessionId;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mediaSessionId = getArgument(MEDIA_SESSION_ID_EXTRA);
		
		getSupportActionBar().setTitle(R.string.moviesAndSeries);
		setThreshold(3);
		
		List<WatchableType> watchableTypes = getArgument(WATCHABLES_TYPES_EXTRA);
		watchablesSuggestionsUseCase = getInstance(WatchablesSuggestionsUseCase.class);
		watchablesSuggestionsUseCase.setWatchableTypes(watchableTypes);
		
		addManualSelectionUseCase = getInstance(AddManualSelectionUseCase.class);
		addManualSelectionUseCase.setMediaSessionId(mediaSessionId);
		addManualSelectionUseCaseListener = new AndroidUseCaseListener() {
			
			@Override
			public Boolean goBackOnError(RuntimeException runtimeException) {
				return false;
			}
			
			@Override
			public void onFinishUseCase() {
				executeOnUIThread(new Runnable() {
					
					@Override
					public void run() {
						dismissLoading();
						getActivity().setResult(Activity.RESULT_OK);
						getActivity().finish();
					}
				});
			}
			
			@Override
			protected ActivityIf getActivityIf() {
				return (ActivityIf)getActivity();
			}
		};
	}
	
	/**
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.search_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(addManualSelectionUseCase, addManualSelectionUseCaseListener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(addManualSelectionUseCase, addManualSelectionUseCaseListener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#getSearchUseCase()
	 */
	@Override
	protected SearchUseCase<Watchable> getSearchUseCase() {
		return watchablesSuggestionsUseCase;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#createBaseArrayAdapter()
	 */
	@Override
	protected BaseArrayAdapter<Watchable> createBaseArrayAdapter() {
		return new WatchableAdapter(getActivity(), Lists.<Watchable>newArrayList());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#isSearchValueRequired()
	 */
	@Override
	public boolean isSearchValueRequired() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(final Watchable watchable) {
		addManualSelectionUseCase.setWatchable(watchable);
		executeUseCase(addManualSelectionUseCase);
	}
}
