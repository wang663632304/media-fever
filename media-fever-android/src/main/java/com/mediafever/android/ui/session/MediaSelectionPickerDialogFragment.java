package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.usecase.mediasession.MediaSessionSetupUseCase;
import com.mediafever.usecase.mediasession.SmartSelectionUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionPickerDialogFragment extends AbstractDialogFragment {
	
	private SmartSelectionUseCase smartSelectionUseCase;
	
	public static void show(Fragment targetFragment) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		MediaSelectionPickerDialogFragment dialogFragment = new MediaSelectionPickerDialogFragment();
		dialogFragment.setTargetFragment(targetFragment, 1);
		dialogFragment.show(fm, MediaSelectionPickerDialogFragment.class.getSimpleName());
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Google TV is not displaying the title of the dialog.
		if (AndroidUtils.isGoogleTV()) {
			setStyle(STYLE_NO_TITLE, 0);
		}
		
		if (smartSelectionUseCase == null) {
			smartSelectionUseCase = getInstance(SmartSelectionUseCase.class);
			smartSelectionUseCase.setMediaSession(getMediaSessionSetupUseCase().getMediaSession());
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.media_selection_picker_dialog_fragment, container, false);
		
		View manualSelection = view.findViewById(R.id.manualSelection);
		manualSelection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO
			}
		});
		
		View smartSelection = view.findViewById(R.id.smartSelection);
		smartSelection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				executeUseCase(smartSelectionUseCase);
			}
		});
		
		getDialog().setTitle(R.string.selection);
		return view;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(smartSelectionUseCase, this);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(smartSelectionUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				getMediaSessionSetupUseCase().addSelection(smartSelectionUseCase.getWatchable());
				((MediaSelectionsFragment)getTargetFragment()).refresh();
				dismissLoading();
				dismiss();
			}
		});
	}
	
	public MediaSessionSetupUseCase getMediaSessionSetupUseCase() {
		return ((MediaSessionActivity)getActivity()).getMediaSessionSetupUseCase();
	}
}
