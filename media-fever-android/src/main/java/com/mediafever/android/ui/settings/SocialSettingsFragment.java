package com.mediafever.android.ui.settings;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.jdroid.android.facebook.MultipleUsersSharedPreferencesTokenCachingStrategy;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.utils.ToastUtils;
import com.mediafever.R;
import com.mediafever.context.ApplicationContext;
import com.mediafever.usecase.settings.ConnectToFacebookUseCase;

/**
 * Fragment that enables the user to connect his MediaFever! account to social media accounts. Currently only Facebook
 * is supported.
 * 
 * @author Maxi Rosson
 */
public class SocialSettingsFragment extends AbstractFragment {
	
	private static final String TAG = SocialSettingsFragment.class.getSimpleName();
	
	private ConnectToFacebookUseCase connectToFacebookUseCase;
	
	// TODO: See if we can change Facebook login button for our own implementation using SwitchButton.
	@InjectView(R.id.authButton)
	private LoginButton loginButton;
	
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.social_settings_fragment, container, false);
		loginButton = LoginButton.class.cast(view.findViewById(R.id.authButton));
		loginButton.setFragment(this);
		loginButton.setApplicationId(ApplicationContext.get().getFacebookAppId());
		
		return view;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		MultipleUsersSharedPreferencesTokenCachingStrategy tokenCachingStrategy = new MultipleUsersSharedPreferencesTokenCachingStrategy(
				getActivity());
		tokenCachingStrategy.buildActiveSession(getActivity(), ApplicationContext.get().getFacebookAppId());
		
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		
		if (connectToFacebookUseCase == null) {
			connectToFacebookUseCase = getInstance(ConnectToFacebookUseCase.class);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		onResumeUseCase(connectToFacebookUseCase, this, UseCaseTrigger.MANUAL);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		onPauseUseCase(connectToFacebookUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	/**
	 * Connects/Disconnects the MediaFever account from a FB account based on FB's {@link Session} state.
	 * 
	 * @param session FB's {@link Session}
	 * @param state The new state for FB's {@link Session}.
	 * @param exception The exception that may have been thrown when trying to change the {@link Session}'s state.
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Log.i(TAG, "Facebook session state changed to " + state.name());
		
		if (SessionState.OPENED_TOKEN_UPDATED.equals(state)) {
			// Refresh FB token in the server if its updated here.
			connectToFacebook(session);
		} else if (SessionState.CLOSED.equals(state)) {
			// Session was closed, remove token from server.
			executeUseCase(connectToFacebookUseCase);
		}
	}
	
	/**
	 * Links the user's account to the given Facebook session.
	 * 
	 * @param session The session to link.
	 */
	private void connectToFacebook(Session session) {
		connectToFacebookUseCase.setDataToConnect(session.getAccessToken(), session.getExpirationDate());
		executeUseCase(connectToFacebookUseCase);
	}
	
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
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE) && (resultCode == Activity.RESULT_OK)) {
			connectToFacebook(Session.getActiveSession());
		}
	}
}
