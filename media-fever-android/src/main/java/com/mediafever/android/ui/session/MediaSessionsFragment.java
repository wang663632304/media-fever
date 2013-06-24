package com.mediafever.android.ui.session;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.commonsware.cwac.merge.MergeAdapter;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.view.ViewBuilder;
import com.mediafever.R;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.mediasession.MediaSessionsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionsFragment extends AbstractListFragment<MediaSession> {
	
	private MediaSessionsUseCase mediaSessionsUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle(R.string.mediaSessions);
		mediaSessionsUseCase = getInstance(MediaSessionsUseCase.class);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.media_sessions_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(MediaSession mediaSession) {
		if (mediaSession.isPending()) {
			AcceptRejectSessionDialogFragment.show(mediaSession.getId(), this);
		} else {
			MediaSelectionsActivity.start(getActivity(), mediaSession.getId(), false);
		}
	}
	
	public void onMediaSessionAccepted(MediaSession mediaSession) {
		MediaSelectionsActivity.start(getActivity(), mediaSession.getId(), false);
	}
	
	public void onMediaSessionRejected(MediaSession mediaSession) {
		executeUseCase(mediaSessionsUseCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(mediaSessionsUseCase, this, UseCaseTrigger.ALWAYS);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(mediaSessionsUseCase, this);
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
				List<MediaSession> pendingMediaSessions = mediaSessionsUseCase.getPendingMediaSessions();
				if (!pendingMediaSessions.isEmpty()) {
					mergeAdapter.addView(ViewBuilder.buildSectionTitle(activity, R.string.pendingSessions));
					mergeAdapter.addAdapter(new MediaSessionAdapter(activity, pendingMediaSessions, getUser()));
				}
				
				List<MediaSession> activeMediaSessions = mediaSessionsUseCase.getActiveMediaSessions();
				if (!activeMediaSessions.isEmpty()) {
					mergeAdapter.addView(ViewBuilder.buildSectionTitle(activity, R.string.activeSessions));
					mergeAdapter.addAdapter(new MediaSessionAdapter(activity, activeMediaSessions, getUser()));
				}
				
				List<MediaSession> expiredMediaSessions = mediaSessionsUseCase.getExpiredMediaSessions();
				if (!expiredMediaSessions.isEmpty()) {
					mergeAdapter.addView(ViewBuilder.buildSectionTitle(activity, R.string.expiredSessions));
					mergeAdapter.addAdapter(new MediaSessionAdapter(activity, expiredMediaSessions, getUser()));
				}
				
				setListAdapter(mergeAdapter);
				dismissLoading();
			}
		});
	}
}