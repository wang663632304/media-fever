package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.mediafever.R;
import com.mediafever.usecase.mediasession.AcceptMediaSessionUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class AcceptRejectSessionDialogFragment extends AbstractDialogFragment {
	
	private static final String MEDIA_SESSION_ID_EXTRA = "mediaSessionId";
	
	private AcceptMediaSessionUseCase acceptMediaSessionUseCase;
	private Button accept;
	private Button reject;
	
	public static void show(Long mediaSessionId, Fragment targetFragment) {
		FragmentManager fm = targetFragment.getActivity().getSupportFragmentManager();
		
		AcceptRejectSessionDialogFragment dialogFragment = new AcceptRejectSessionDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(MEDIA_SESSION_ID_EXTRA, mediaSessionId);
		dialogFragment.setArguments(bundle);
		
		dialogFragment.setTargetFragment(targetFragment, 1);
		dialogFragment.show(fm, AcceptRejectSessionDialogFragment.class.getSimpleName());
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
		
		Long mediaSessionId = getArgument(MEDIA_SESSION_ID_EXTRA);
		
		acceptMediaSessionUseCase = getInstance(AcceptMediaSessionUseCase.class);
		acceptMediaSessionUseCase.setMediaSessionId(mediaSessionId);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.accept_reject_dialog_fragment, container, false);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		accept = findView(R.id.accept);
		accept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				acceptMediaSessionUseCase.setAsAccepted();
				executeUseCase(acceptMediaSessionUseCase);
			}
		});
		
		reject = findView(R.id.reject);
		reject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				acceptMediaSessionUseCase.setAsRejected();
				executeUseCase(acceptMediaSessionUseCase);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(acceptMediaSessionUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
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
				dismissLoading();
				dismiss();
				((MediaSessionsFragment)getTargetFragment()).refresh();
			}
		});
	}
}
