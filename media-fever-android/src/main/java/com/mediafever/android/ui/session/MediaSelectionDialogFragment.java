package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSessionUser;
import com.mediafever.usecase.mediasession.MediaSessionSetupUseCase;
import com.mediafever.usecase.mediasession.VoteMediaSelectionUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionDialogFragment extends AbstractDialogFragment {
	
	private static final String MEDIA_SELECTION_EXTRA = "mediaSelection";
	
	private VoteMediaSelectionUseCase voteMediaSelectionUseCase;
	private MediaSelection mediaSelection;
	
	public static void show(Fragment targetFragment, MediaSelection mediaSelection) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		MediaSelectionDialogFragment dialogFragment = new MediaSelectionDialogFragment(mediaSelection);
		dialogFragment.setTargetFragment(targetFragment, 1);
		dialogFragment.show(fm, MediaSelectionDialogFragment.class.getSimpleName());
	}
	
	public MediaSelectionDialogFragment() {
	}
	
	public MediaSelectionDialogFragment(MediaSelection mediaSelection) {
		this.mediaSelection = mediaSelection;
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(MEDIA_SELECTION_EXTRA, mediaSelection);
		setArguments(bundle);
	}
	
	public MediaSessionSetupUseCase getMediaSessionSetupUseCase() {
		return ((MediaSessionActivity)getActivity()).getMediaSessionSetupUseCase();
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		// Google TV is not displaying the title of the dialog.
		if (AndroidUtils.isGoogleTV()) {
			setStyle(STYLE_NO_TITLE, 0);
		}
		
		Bundle args = getArguments();
		if (args != null) {
			mediaSelection = (MediaSelection)args.getSerializable(MEDIA_SELECTION_EXTRA);
		}
		
		if (voteMediaSelectionUseCase == null) {
			voteMediaSelectionUseCase = getInstance(VoteMediaSelectionUseCase.class);
			voteMediaSelectionUseCase.setMediaSelection(mediaSelection);
			voteMediaSelectionUseCase.setMediaSession(getMediaSessionSetupUseCase().getMediaSession());
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.media_selection_dialog_fragment, container, false);
		
		Button details = (Button)view.findViewById(R.id.details);
		Button remove = (Button)view.findViewById(R.id.remove);
		Button change = (Button)view.findViewById(R.id.change);
		Button thumbsUp = (Button)view.findViewById(R.id.thumbsUp);
		Button thumbsDown = (Button)view.findViewById(R.id.thumbsDown);
		
		details.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WatchableAdapter.onItemClick(getActivity(), mediaSelection.getWatchable());
			}
		});
		
		if (mediaSelection.getOwner().equals(SecurityContext.get().getUser())) {
			remove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO
				}
			});
			change.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// MediaSelectionPickerDialogFragment.show(this);
				}
			});
			
			thumbsUp.setVisibility(View.GONE);
			thumbsDown.setVisibility(View.GONE);
		} else {
			remove.setVisibility(View.GONE);
			change.setVisibility(View.GONE);
			
			thumbsUp.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					voteMediaSelectionUseCase.setThumbsUp(true);
					executeUseCase(voteMediaSelectionUseCase);
				}
			});
			
			MediaSessionUser mediaSessionUser = getMediaSessionSetupUseCase().getMediaSession().getMe();
			thumbsUp.setVisibility(mediaSessionUser.hasPendingThumbsUp() ? View.VISIBLE : View.GONE);
			
			thumbsDown.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					voteMediaSelectionUseCase.setThumbsUp(false);
					executeUseCase(voteMediaSelectionUseCase);
				}
			});
			thumbsDown.setVisibility(mediaSessionUser.hasPendingThumbsDown() ? View.VISIBLE : View.GONE);
		}
		
		getDialog().setTitle(mediaSelection.getWatchable().getName());
		return view;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(voteMediaSelectionUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(voteMediaSelectionUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				((MediaSelectionsFragment)getTargetFragment()).refresh();
				dismissLoading();
				dismiss();
			}
		});
	}
}
