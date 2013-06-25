package com.mediafever.android.ui.session;

import java.util.List;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.commonsware.cwac.merge.MergeAdapter;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.view.ViewBuilder;
import com.jdroid.java.collections.Lists;
import com.mediafever.R;
import com.mediafever.android.gcm.GcmMessage;
import com.mediafever.android.gcm.GcmMessageBroadcastReceiver;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.mediasession.MediaSessionsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionsFragment extends AbstractListFragment<MediaSession> {
	
	private MediaSessionsUseCase mediaSessionsUseCase;
	private Boolean showLoading = true;
	
	private BroadcastReceiver refreshBroadcastReceiver;
	private List<GcmMessage> messagesToListen = Lists.newArrayList(GcmMessage.MEDIA_SESSION_INVITATION,
		GcmMessage.MEDIA_SELECTION_ADDED, GcmMessage.MEDIA_SELECTION_REMOVED, GcmMessage.MEDIA_SELECTION_THUMBS_DOWN,
		GcmMessage.MEDIA_SELECTION_THUMBS_UP, GcmMessage.MEDIA_SESSION_EXPIRED, GcmMessage.MEDIA_SESSION_LEFT,
		GcmMessage.MEDIA_SESSION_UPDATED);
	
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
		
		refreshBroadcastReceiver = new GcmMessageBroadcastReceiver(messagesToListen) {
			
			@Override
			protected void onGcmMessage(GcmMessage gcmMessage, Intent intent) {
				executeUseCase(mediaSessionsUseCase);
			}
		};
		
		GcmMessage.startListeningMediaSessionSynchBroadcasts(refreshBroadcastReceiver);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(mediaSessionsUseCase, this);
		
		GcmMessage.stopListeningMediaSessionSynchBroadcasts(refreshBroadcastReceiver);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		if (showLoading) {
			super.onStartUseCase();
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
				
				showLoading = false;
			}
		});
	}
}