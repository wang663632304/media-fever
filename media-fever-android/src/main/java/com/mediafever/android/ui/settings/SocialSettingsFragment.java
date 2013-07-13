package com.mediafever.android.ui.settings;

import org.slf4j.Logger;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.facebook.Session;
import com.google.ads.AdSize;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.R;

/**
 * Fragment that enables the user to connect his MediaFever! account to social media accounts. Currently only Facebook
 * is supported.
 * 
 * @author Maxi Rosson
 */
public class SocialSettingsFragment extends AbstractFragment {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(SocialSettingsFragment.class);
	
	// private ConnectToFacebookUseCase connectToFacebookUseCase;
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.social_settings_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		((AbstractFragmentActivity)getActivity()).addFragment(new SocialSettingsHelperFragment(), 0, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		findView(R.id.authButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SocialSettingsHelperFragment socialSettingsHelperFragment = ((AbstractFragmentActivity)getActivity()).getFragment(SocialSettingsHelperFragment.class);
				socialSettingsHelperFragment.startLoginProcess();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		// onResumeUseCase(connectToFacebookUseCase, this, UseCaseTrigger.MANUAL);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		// onPauseUseCase(connectToFacebookUseCase, this);
	}
	
	/**
	 * Connects/Disconnects the MediaFever account from a FB account based on FB's {@link Session} state.
	 * 
	 * @param session FB's {@link Session}
	 * @param state The new state for FB's {@link Session}.
	 * @param exception The exception that may have been thrown when trying to change the {@link Session}'s state.
	 */
	// private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	// LOGGER.debug("Facebook session state changed to " + state.name());
	//
	// if (SessionState.OPENED_TOKEN_UPDATED.equals(state)) {
	// // Refresh FB token in the server if its updated here.
	// connectToFacebook(session);
	// } else if (SessionState.CLOSED.equals(state)) {
	// // Session was closed, remove token from server.
	// executeUseCase(connectToFacebookUseCase);
	// }
	// }
	
	/**
	 * Links the user's account to the given Facebook session.
	 * 
	 * @param session The session to link.
	 */
	// private void connectToFacebook(Session session) {
	// connectToFacebookUseCase.setDataToConnect(session.getAccessToken(), session.getExpirationDate());
	// executeUseCase(connectToFacebookUseCase);
	// }
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				ToastUtils.showInfoToast(Session.getActiveSession().isOpened() ? R.string.accountConnected
						: R.string.accountDisconnected);
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? null : super.getAdSize();
	}
}
