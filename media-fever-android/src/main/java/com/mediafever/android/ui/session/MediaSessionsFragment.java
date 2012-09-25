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
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.AcceptMediaSessionUseCase;
import com.mediafever.usecase.MediaSessionsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionsFragment extends AbstractListFragment<MediaSession> {
	
	private MediaSessionsUseCase mediaSessionsUseCase;
	private AcceptMediaSessionUseCase acceptMediaSessionUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		getSupportActionBar().setTitle(R.string.mediaSessions);
		
		if (mediaSessionsUseCase == null) {
			mediaSessionsUseCase = getInstance(MediaSessionsUseCase.class);
			mediaSessionsUseCase.setUserId(getUser().getId());
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
	public void onItemSelected(MediaSession mediaSession) {
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
		return R.string.noResultsMediaSessions;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setItemsCanFocus(true);
		
		if (acceptMediaSessionUseCase == null) {
			acceptMediaSessionUseCase = getInstance(AcceptMediaSessionUseCase.class);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(mediaSessionsUseCase, this, true);
		onResumeUseCase(acceptMediaSessionUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(mediaSessionsUseCase, this);
		onPauseUseCase(acceptMediaSessionUseCase, this);
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
				
				Activity activity = MediaSessionsFragment.this.getActivity();
				mergeAdapter.addView(new ListSeparatorView(activity, R.string.pendingSessions));
				mergeAdapter.addAdapter(new MediaSessionAdapter(activity,
						mediaSessionsUseCase.getPendingMediaSessions()) {
					
					@Override
					public void onAccept(MediaSession mediaSession) {
						acceptMediaSessionUseCase.setMediaSession(mediaSession);
						acceptMediaSessionUseCase.setAsAccepted();
						executeUseCase(acceptMediaSessionUseCase);
					}
					
					@Override
					public void onReject(MediaSession mediaSession) {
						acceptMediaSessionUseCase.setMediaSession(mediaSession);
						acceptMediaSessionUseCase.setAsRejected();
						executeUseCase(acceptMediaSessionUseCase);
					}
				});
				
				mergeAdapter.addView(new ListSeparatorView(activity, R.string.acceptedSessions));
				mergeAdapter.addAdapter(new MediaSessionAdapter(activity,
						mediaSessionsUseCase.getAcceptedMediaSessions()));
				
				setListAdapter(mergeAdapter);
				dismissLoading();
			}
		});
	}
}