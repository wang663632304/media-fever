package com.mediafever.android.ui.session;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.view.MenuItem;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.animation.FadeInOutAnimation;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.UriFileContent;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.images.CustomImageView;
import com.jdroid.android.utils.AlertDialogUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.gcm.GcmMessage;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.mediasession.MediaSessionDetailsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsFragment extends AbstractGridFragment<MediaSelection> {
	
	private static final String TAG = MediaSelectionsFragment.class.getSimpleName();
	
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
		
		refresh();
	}
	
	public static void synchronize(Bundle bundle) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(SYNCHRONIZE_ACTION);
		broadcastIntent.putExtras(bundle);
		LocalBroadcastManager.getInstance(AbstractApplication.get()).sendBroadcast(broadcastIntent);
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
				
				String mediaSessionId = intent.getStringExtra(GcmMessage.MEDIA_SESSION_ID_KEY);
				if (mediaSessionId.equals(mediaSession.getId().toString())) {
					mediaSessionDetailsUseCase.setSynch(true);
					executeUseCase(mediaSessionDetailsUseCase);
					
					GcmMessage gcmMessage = GcmMessage.find(intent);
					if (!gcmMessage.equals(GcmMessage.MEDIA_SESSION_UPDATED)) {
						FileContent imageContent = new UriFileContent(intent.getStringExtra(GcmMessage.IMAGE_URL_KEY));
						CustomImageView customImageView = findView(R.id.userImage);
						customImageView.setImageContent(imageContent, R.drawable.user_default);
						
						String watchableName = intent.getStringExtra(GcmMessage.WATCHABLE_NAME_KEY);
						
						String fullName = intent.getStringExtra(GcmMessage.FULL_NAME_KEY);
						TextView synchMessage = findView(R.id.synchMessage);
						if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_ADDED)) {
							synchMessage.setText(getString(R.string.mediaSelectionAdded, fullName, watchableName));
						} else if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_REMOVED)) {
							synchMessage.setText(getString(R.string.mediaSelectionRemoved, fullName, watchableName));
						} else if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_THUMBS_UP)) {
							synchMessage.setText(getString(R.string.mediaSelectionThumbsUp, fullName, watchableName));
						} else if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_THUMBS_DOWN)) {
							synchMessage.setText(getString(R.string.mediaSelectionThumbsDown, fullName, watchableName));
						}
						
						ViewGroup mediaSelectionsSynch = findView(R.id.mediaSelectionsSynch);
						mediaSelectionsSynch.clearAnimation();
						mediaSelectionsSynch.startAnimation(new FadeInOutAnimation(mediaSelectionsSynch, 1000, 4000));
					}
					
				}
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
		Log.e(TAG, MediaSessionDetailsUseCase.class.getSimpleName() + " failed when executed.", runtimeException);
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
		
		// Header
		TextView header = findView(R.id.header);
		header.setText(getString(R.string.mediaSelectionsHeader, getWatchablesString()));
		
		// Footer
		TextView footer = findView(R.id.mediaSelectionsStarts);
		String dateTime = MediaSessionAdapter.getDateString(mediaSession);
		if (StringUtils.isNotEmpty(dateTime)) {
			footer.setText(getString(R.string.mediaSelectionsStarts, dateTime));
			footer.setVisibility(View.VISIBLE);
		} else {
			footer.setVisibility(View.GONE);
		}
		
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
