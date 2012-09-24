package com.mediafever.android.ui.session;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.commonsware.cwac.merge.MergeAdapter;
import com.google.ads.AdSize;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.view.ListSeparatorView;
import com.mediafever.R;
import com.mediafever.android.ui.home.HomeActivity;
import com.mediafever.domain.watchingsession.WatchingSession;
import com.mediafever.usecase.AcceptWatchingSessionUseCase;
import com.mediafever.usecase.WatchingSessionsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionsFragment extends AbstractListFragment<WatchingSession> {
	
	private WatchingSessionsUseCase watchingSessionsUseCase;
	private AcceptWatchingSessionUseCase acceptWatchingSessionUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		getSupportActionBar().setTitle(R.string.watchingSessions);
		
		if (watchingSessionsUseCase == null) {
			watchingSessionsUseCase = getInstance(WatchingSessionsUseCase.class);
			watchingSessionsUseCase.setUserId(getUser().getId());
		}
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
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(WatchingSession watchingSession) {
		// TODO Change the target activity
		ActivityLauncher.launchActivity(HomeActivity.class);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? AdSize.IAB_BANNER : AdSize.SMART_BANNER;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#getNoResultsText()
	 */
	@Override
	protected int getNoResultsText() {
		return R.string.noResultsWatchingSessions;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setItemsCanFocus(true);
		
		if (acceptWatchingSessionUseCase == null) {
			acceptWatchingSessionUseCase = getInstance(AcceptWatchingSessionUseCase.class);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(watchingSessionsUseCase, this, true);
		onResumeUseCase(acceptWatchingSessionUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(watchingSessionsUseCase, this);
		onPauseUseCase(acceptWatchingSessionUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				
				MergeAdapter mergeAdapter = new MergeAdapter();
				
				Activity activity = WatchingSessionsFragment.this.getActivity();
				mergeAdapter.addView(new ListSeparatorView(activity, R.string.pendingSessions));
				mergeAdapter.addAdapter(new WatchingSessionAdapter(activity,
						watchingSessionsUseCase.getPendingWatchingSessions()) {
					
					@Override
					public void onAccept(WatchingSession watchingSession) {
						acceptWatchingSessionUseCase.setWatchingSession(watchingSession);
						acceptWatchingSessionUseCase.setAsAccepted();
						executeUseCase(acceptWatchingSessionUseCase);
					}
					
					@Override
					public void onReject(WatchingSession watchingSession) {
						acceptWatchingSessionUseCase.setWatchingSession(watchingSession);
						acceptWatchingSessionUseCase.setAsRejected();
						executeUseCase(acceptWatchingSessionUseCase);
					}
				});
				
				mergeAdapter.addView(new ListSeparatorView(activity, R.string.acceptedSessions));
				mergeAdapter.addAdapter(new WatchingSessionAdapter(activity,
						watchingSessionsUseCase.getAcceptedWatchingSessions()));
				
				setListAdapter(mergeAdapter);
				dismissLoading();
			}
		});
	}
}