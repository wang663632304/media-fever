package com.mediafever.android.ui.session;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.AndroidUseCaseListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.animation.FadeInOutAnimation;
import com.jdroid.android.dialog.AlertDialogFragment;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.UriFileContent;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.images.CustomImageView;
import com.jdroid.android.images.CustomImageView.ImageLoadingListener;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.gcm.GcmMessage;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.mediasession.MediaSessionDetailsUseCase;
import com.mediafever.usecase.mediasession.MediaSessionLeaveUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsFragment extends AbstractGridFragment<MediaSelection> {
	
	private static final String SYNCHRONIZE_ACTION = MediaSelectionsFragment.class.getSimpleName()
			+ ".SYNCHRONIZE_ACTION";
	
	public static final int MEDIA_SELECTION_ADDED_REQUEST_CODE = 1;
	
	public static final String MEDIA_SESSION_ID_EXTRA = "mediaSessionIdExtra";
	public static final String MEDIA_SESSION_CREATED_EXTRA = "mediaSessionCreatedExtra";
	
	private MediaSessionDetailsUseCase mediaSessionDetailsUseCase;
	private MediaSessionLeaveUseCase mediaSessionLeaveUseCase;
	private AndroidUseCaseListener mediaSessionLeaveUseCaseListener;
	
	private BroadcastReceiver refreshBroadcastReceiver;
	private Long mediaSessionId;
	private Boolean mediaSessionCreated;
	private Boolean mediaSessionLoaded = false;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mediaSessionId = getArgument(MEDIA_SESSION_ID_EXTRA);
		mediaSessionCreated = getArgument(MEDIA_SESSION_CREATED_EXTRA);
		
		mediaSessionDetailsUseCase = getInstance(MediaSessionDetailsUseCase.class);
		mediaSessionDetailsUseCase.setMediaSessionId(mediaSessionId);
		
		mediaSessionLeaveUseCase = getInstance(MediaSessionLeaveUseCase.class);
		mediaSessionLeaveUseCase.setMediaSessionId(mediaSessionId);
		mediaSessionLeaveUseCaseListener = new AndroidUseCaseListener() {
			
			@Override
			public void onFinishUseCase() {
				executeOnUIThread(new Runnable() {
					
					@Override
					public void run() {
						getActivity().finish();
						dismissLoading();
					}
				});
			}
			
			@Override
			public Boolean goBackOnError(RuntimeException runtimeException) {
				return false;
			}
			
			@Override
			protected ActivityIf getActivityIf() {
				return (ActivityIf)getActivity();
			}
		};
		
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
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		MediaSession mediaSession = mediaSessionDetailsUseCase.getMediaSession();
		if (mediaSession != null) {
			refresh(mediaSession);
		}
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
		onResumeUseCase(mediaSessionDetailsUseCase, this, UseCaseTrigger.ONCE);
		onResumeUseCase(mediaSessionLeaveUseCase, mediaSessionLeaveUseCaseListener);
		
		refreshBroadcastReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, final Intent intent) {
				
				String notificationMediaSessionId = intent.getStringExtra(GcmMessage.MEDIA_SESSION_ID_KEY);
				if (notificationMediaSessionId.equals(mediaSessionId.toString())) {
					executeUseCase(mediaSessionDetailsUseCase);
					
					final GcmMessage gcmMessage = GcmMessage.find(intent);
					if (!gcmMessage.equals(GcmMessage.MEDIA_SESSION_UPDATED)
							&& !gcmMessage.equals(GcmMessage.MEDIA_SESSION_EXPIRED)) {
						CustomImageView customImageView = findView(R.id.synchUserImage);
						customImageView.setImageLoadingListener(new ImageLoadingListener() {
							
							@Override
							public void onImageLoaded() {
								displayNotification(intent, gcmMessage);
							}
							
							@Override
							public void onStubImageLoaded() {
								displayNotification(intent, gcmMessage);
							}
							
						});
						FileContent imageContent = new UriFileContent(intent.getStringExtra(GcmMessage.IMAGE_URL_KEY));
						customImageView.setImageContent(imageContent, R.drawable.user_default);
					}
				}
			}
			
			private void displayNotification(Intent intent, GcmMessage gcmMessage) {
				String watchableName = intent.getStringExtra(GcmMessage.WATCHABLE_NAME_KEY);
				
				String fullName = intent.getStringExtra(GcmMessage.FULL_NAME_KEY);
				TextView synchMessage = findView(R.id.synchMessage);
				if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_ADDED)) {
					synchMessage.setText(getString(R.string.mediaSelectionAdded, fullName, watchableName));
				} else if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_REMOVED)) {
					synchMessage.setText(getString(R.string.mediaSelectionRemoved, fullName, watchableName));
					MediaSelectionDialogFragment.dismiss(getActivity());
				} else if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_THUMBS_UP)) {
					synchMessage.setText(getString(R.string.mediaSelectionThumbsUp, fullName, watchableName));
				} else if (gcmMessage.equals(GcmMessage.MEDIA_SELECTION_THUMBS_DOWN)) {
					synchMessage.setText(getString(R.string.mediaSelectionThumbsDown, fullName, watchableName));
				} else if (gcmMessage.equals(GcmMessage.MEDIA_SESSION_LEFT)) {
					synchMessage.setText(getString(R.string.mediaSessionLeft, fullName));
				}
				ViewGroup mediaSelectionsSynch = findView(R.id.mediaSelectionsSynch);
				mediaSelectionsSynch.clearAnimation();
				mediaSelectionsSynch.startAnimation(new FadeInOutAnimation(mediaSelectionsSynch, 1000, 4000));
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
		onPauseUseCase(mediaSessionLeaveUseCase, mediaSessionLeaveUseCaseListener);
		
		if (refreshBroadcastReceiver != null) {
			LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(refreshBroadcastReceiver);
		}
	}
	
	/**
	 * @see com.actionbarsherlock.app.SherlockFragment#onPrepareOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		MediaSession mediaSession = mediaSessionDetailsUseCase.getMediaSession();
		if ((mediaSession != null) && mediaSession.isExpired()) {
			MenuItem menuItem = menu.findItem(R.id.editMediaSessionItem);
			menuItem.setVisible(false);
		}
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.editMediaSessionItem:
				MediaSessionActivity.start(getActivity(), mediaSessionId);
				return true;
			case R.id.leaveMediaSessionItem:
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.leaveMediaSession);
				builder.setMessage(R.string.leaveMediaSessionConfirmation);
				builder.setPositiveButton(R.string.yes, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						executeUseCase(mediaSessionLeaveUseCase);
					}
				});
				builder.setNegativeButton(R.string.no, null);
				builder.show();
				
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
		MediaSession mediaSession = mediaSessionDetailsUseCase.getMediaSession();
		if (mediaSelection.getWatchable() != null) {
			MediaSelectionDialogFragment.show(this, mediaSessionId, mediaSelection, mediaSession.isExpired());
		} else {
			MediaSelectionPickerDialogFragment.show(this, mediaSessionId, mediaSession.getWatchableTypes());
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return !mediaSessionLoaded;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		if (mediaSessionLoaded) {
			AbstractApplication.get().getExceptionHandler().logHandledException(runtimeException);
			dismissLoadingOnUIThread();
		} else {
			super.onFinishFailedUseCase(runtimeException);
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
				mediaSessionLoaded = true;
				MediaSession mediaSession = mediaSessionDetailsUseCase.getMediaSession();
				if (mediaSessionCreated) {
					AlertDialogFragment.show(MediaSelectionsFragment.this,
						getString(R.string.mediaSessionCreatedTitle),
						getString(R.string.mediaSessionCreatedDescription, getWatchablesString(mediaSession)),
						LocalizationUtils.getString(R.string.ok), null, true);
					mediaSessionCreated = false;
				} else if (mediaSession.isExpired()) {
					MediaSelection mediaSelection = mediaSession.getMediaSelectionWinner();
					if (mediaSelection != null) {
						MediaSelectionExpiredDialogFragment.show(MediaSelectionsFragment.this, mediaSelection);
					}
				}
				getActivity().supportInvalidateOptionsMenu();
				refresh(mediaSession);
				dismissLoading();
			}
		});
	}
	
	private void refresh(MediaSession mediaSession) {
		
		// Header
		TextView header = findView(R.id.header);
		if (mediaSession.isExpired()) {
			header.setText(R.string.mediaSelectionsExpiredHeader);
		} else {
			header.setText(getString(R.string.mediaSelectionsHeader, getWatchablesString(mediaSession)));
		}
		
		// Footer
		TextView footer = findView(R.id.mediaSelectionsStarts);
		
		String dateTime = MediaSessionAdapter.getDateString(mediaSession);
		if (!mediaSession.isExpired() && StringUtils.isNotEmpty(dateTime)) {
			footer.setText(getString(R.string.mediaSelectionsStarts, dateTime));
			footer.setVisibility(View.VISIBLE);
		} else {
			footer.setVisibility(View.GONE);
		}
		
		setListAdapter(new MediaSelectionAdapter(MediaSelectionsFragment.this.getActivity(),
				mediaSession.getSelections()));
	}
	
	private String getWatchablesString(MediaSession mediaSession) {
		if (mediaSession.acceptOnlyMovies()) {
			return getString(R.string.movies).toLowerCase();
		} else if (mediaSession.acceptOnlySeries()) {
			return getString(R.string.series).toLowerCase();
		} else {
			return getString(R.string.moviesOrSeries);
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((resultCode == Activity.RESULT_OK) && (requestCode == MEDIA_SELECTION_ADDED_REQUEST_CODE)) {
			executeUseCase(mediaSessionDetailsUseCase);
		}
	}
}
