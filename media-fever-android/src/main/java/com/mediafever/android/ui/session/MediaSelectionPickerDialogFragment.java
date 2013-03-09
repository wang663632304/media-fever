package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.mediafever.R;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.mediasession.AddSmartSelectionUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionPickerDialogFragment extends AbstractDialogFragment {
	
	private static final String MEDIA_SESSION_EXTRA = "mediaSession";
	
	private AddSmartSelectionUseCase addSmartSelectionUseCase;
	private MediaSession mediaSession;
	
	public static void show(Fragment targetFragment, MediaSession mediaSession) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		MediaSelectionPickerDialogFragment dialogFragment = new MediaSelectionPickerDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(MEDIA_SESSION_EXTRA, mediaSession);
		dialogFragment.setArguments(bundle);
		dialogFragment.setTargetFragment(targetFragment, 1);
		dialogFragment.show(fm, MediaSelectionPickerDialogFragment.class.getSimpleName());
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mediaSession = getArgument(MEDIA_SESSION_EXTRA);
		addSmartSelectionUseCase = getInstance(AddSmartSelectionUseCase.class);
		addSmartSelectionUseCase.setMediaSession(mediaSession);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.media_selection_picker_dialog_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.manualSelection).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Implement manual selection
			}
		});
		
		findView(R.id.smartSelection).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				executeUseCase(addSmartSelectionUseCase);
			}
		});
		
		getDialog().setTitle(R.string.selection);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(addSmartSelectionUseCase, this);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(addSmartSelectionUseCase, this);
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
