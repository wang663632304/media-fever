package com.mediafever.android.ui.session;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.view.MenuItem;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.utils.AlertDialogUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.mediasession.MediaSessionDetailsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsFragment extends AbstractGridFragment<MediaSelection> {
	
	private static final String SYNCHRONIZE_ACTION = MediaSelectionsFragment.class.getSimpleName()
			+ ".SYNCHRONIZE_ACTION";
	
	public static final int MEDIA_SELECTION_ADDED_REQUEST_CODE = 1;
	
	public static final String MEDIA_SESSION_EXTRA = "mediaSessionExtra";
	public static final String MEDIA_SESSION_CREATED_EXTRA = "mediaSessionCreatedExtra";
	
	private MediaSessionDetailsUseCase mediaSessionDetailsUseCase;
	private BroadcastReceiver refreshBroadcastReceiver;
	private MediaSession mediaSession;
	private Boolean mediaSessionCreated;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mediaSession = getArgument(MEDIA_SESSION_EXTRA);
		mediaSessionCreated = getArgument(MEDIA_SESSION_CREATED_EXTRA);
		
		if (mediaSessionCreated) {
			AlertDialogUtils.showOKDialog(getString(R.string.mediaSessionCreatedTitle),
				getString(R.string.mediaSessionCreatedDescription, getWatchablesString()));
		}
		
		mediaSessionDetailsUseCase = getInstance(MediaSessionDetailsUseCase.class);
		mediaSessionDetailsUseCase.setMediaSessionId(mediaSession.getId());
		
		setHasOptionsMenu(true);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.media_selections_fragment, container, false);
	}
	
	private String getWatchablesString() {
		if (mediaSession.acceptOnlyMovies()) {
			return getString(R.string.movies).toLowerCase();
		} else if (mediaSession.acceptOnlySeries()) {
			return getString(R.string.series).toLowerCase();
		} else {
			return getString(R.string.moviesOrSeries);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// Header
		TextView header = findView(R.id.header);
		header.setText(getString(R.string.mediaSelectionsHeader, getWatchablesString()));
		
		// Footer
		TextView footer = findView(R.id.footer);
		String dateTime = MediaSessionAdapter.getDateString(mediaSession);
		if (StringUtils.isNotEmpty(dateTime)) {
			footer.setText(getString(R.string.mediaSelectionsFooter, dateTime));
			footer.setVisibility(View.VISIBLE);
		} else {
			footer.setVisibility(View.GONE);
		}
		
		refresh();
	}
	
	public static void synchronize(Context context) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(SYNCHRONIZE_ACTION);
		LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(mediaSessionDetailsUseCase, this);
		
		refreshBroadcastReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				mediaSessionDetailsUseCase.setSynch(true);
				executeUseCase(mediaSessionDetailsUseCase);
			}
		};
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(SYNCHRONIZE_ACTION);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(refreshBroadcastReceiver, intentFilter);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(mediaSessionDetailsUseCase, this);
		
		if (refreshBroadcastReceiver != null) {
			LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(refreshBroadcastReceiver);
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.editMediaSessionItem:
				MediaSessionActivity.start(getActivity(), mediaSession.getId());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(MediaSelection mediaSelection) {
		if (mediaSelection.getWatchable() != null) {
			MediaSelectionDialogFragment.show(this, mediaSession, mediaSelection);
		} else {
			MediaSelectionPickerDialogFragment.show(this, mediaSession);
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
				mediaSession = mediaSessionDetailsUseCase.getMediaSession();
				refresh();
				dismissLoading();
			}
		});
	}
	
	private void refresh() {
		setListAdapter(new MediaSelectionAdapter(MediaSelectionsFragment.this.getActivity(),
				mediaSession.getSelections()));
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((resultCode == Activity.RESULT_OK) && (requestCode == MEDIA_SELECTION_ADDED_REQUEST_CODE)) {
			mediaSessionDetailsUseCase.setSynch(false);
			executeUseCase(mediaSessionDetailsUseCase);
		}
	}
}
