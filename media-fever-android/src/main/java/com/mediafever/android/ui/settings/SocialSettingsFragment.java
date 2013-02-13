package com.mediafever.android.ui.settings;

import roboguice.inject.InjectView;
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
import com.jdroid.android.AndroidUseCaseListener;
import com.jdroid.android.fragment.AbstractFragment;
import com.mediafever.R;
import com.mediafever.usecase.settings.ConnectToFacebookUseCase;
import com.mediafever.usecase.settings.FacebookAccountUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialSettingsFragment extends AbstractFragment {
	
	private ConnectToFacebookUseCase connectToFacebookUseCase;
	private FacebookAccountUseCase facebookAccountUseCase;
	private AndroidUseCaseListener facebookAccountUseCaseListener;
	
	// private FacebookConnector facebookConnector = new
	// FacebookConnector(ApplicationContext.get().getFacebookAppId());;
	
	// @InjectView(R.id.connectToFacebook)
	// private SwitchButton connectButton;
	
	@InjectView(R.id.authButton)
	private LoginButton loginButton;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	private UiLifecycleHelper uiHelper;
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.social_settings_fragment, container, false);
		loginButton = LoginButton.class.cast(view.findViewById(R.id.authButton));
		loginButton.setFragment(this);
		
		return view;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		// Session.openActiveSession(getActivity(), this, true, callback);
		uiHelper.onCreate(savedInstanceState);
		
		// if (connectToFacebookUseCase == null) {
		// connectToFacebookUseCase = getInstance(ConnectToFacebookUseCase.class);
		// connectToFacebookUseCase.setFacebookConnector(facebookConnector);
		// }
		//
		// if (facebookAccountUseCase == null) {
		// facebookAccountUseCase = getInstance(FacebookAccountUseCase.class);
		// facebookAccountUseCaseListener = new AndroidUseCaseListener() {
		//
		// @Override
		// public void onFinishUseCase() {
		// executeOnUIThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// FacebookAccount facebookAccount = facebookAccountUseCase.getFacebookAccount();
		// if (facebookAccount != null) {
		// facebookConnector.setAccessToken(facebookAccount.getAccessToken(),
		// facebookAccount.getAccessExpiresIn());
		// connectButton.setChecked(facebookConnector.isConnected());
		// }
		// dismissLoading();
		// }
		// });
		// }
		//
		// @Override
		// protected ActivityIf getActivityIf() {
		// return (ActivityIf)getActivity();
		// }
		// };
		//
		// facebookAccountUseCase.addListener(facebookAccountUseCaseListener);
		// executeUseCase(facebookAccountUseCase);
		//
		// }
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		// onResumeUseCase(connectToFacebookUseCase, this, UseCaseTrigger.MANUAL);
		// onResumeUseCase(facebookAccountUseCase, facebookAccountUseCaseListener, UseCaseTrigger.ALWAYS);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		// onPauseUseCase(connectToFacebookUseCase, this);
		// onPauseUseCase(facebookAccountUseCase, facebookAccountUseCaseListener);
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
	 * @see com.jdroid.android.fragment.AbstractFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// initFacebookAccountUseCase();
		// initConnectToFacebookUseCase();
		
		// connectButton.setChecked(facebookConnector.isConnected());
		//
		// connectButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// Boolean isConnected = facebookConnector.isConnected();
		// connectToFacebookUseCase.setConnect(isChecked);
		//
		// // Connect to Facebook only if the button has been checked and we don't have a connection already.
		// if (isChecked && !isConnected) {
		// facebookConnector.connect(getActivity());
		// } else if (!isChecked && isConnected) {
		// // Disconnect from Facebook only when we are already connected and the button has been unchecked.
		// connectToFacebookUseCase.setContext(getActivity());
		// executeUseCase(connectToFacebookUseCase);
		// }
		// }
		// });
		
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Log.i("Facebook fragment", state.name());
	}
	
	// TODO Use the onResumeUseCase & onPauseUseCase methods
	// private void initConnectToFacebookUseCase() {
	// if (connectToFacebookUseCase == null) {
	// connectToFacebookUseCase = getInstance(ConnectToFacebookUseCase.class);
	// connectToFacebookUseCase.addListener(this);
	// connectToFacebookUseCase.setFacebookConnector(facebookConnector);
	// } else if (connectToFacebookUseCase.isInProgress()) {
	// onStartUseCase();
	// } else if (connectToFacebookUseCase.isFinish()) {
	// onFinishUseCase();
	// }
	
	// }
	
	// TODO Use the onResumeUseCase & onPauseUseCase methods
	// private void initFacebookAccountUseCase() {
	// if (facebookAccountUseCase == null) {
	// facebookAccountUseCase = getInstance(FacebookAccountUseCase.class);
	// facebookAccountUseCaseListener = new AndroidUseCaseListener() {
	//
	// @Override
	// public void onFinishUseCase() {
	// executeOnUIThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// FacebookAccount facebookAccount = facebookAccountUseCase.getFacebookAccount();
	// if (facebookAccount != null) {
	// facebookConnector.setAccessToken(facebookAccount.getAccessToken(),
	// facebookAccount.getAccessExpiresIn());
	// connectButton.setChecked(facebookConnector.isConnected());
	// }
	// dismissLoading();
	// }
	// });
	// }
	//
	// @Override
	// protected ActivityIf getActivityIf() {
	// return (ActivityIf)getActivity();
	// }
	// };
	//
	// facebookAccountUseCase.addListener(facebookAccountUseCaseListener);
	// executeUseCase(facebookAccountUseCase);
	//
	// } else if (facebookAccountUseCase.isInProgress()) {
	// facebookAccountUseCaseListener.onStartUseCase();
	// } else if (facebookAccountUseCase.isFinish()) {
	// facebookAccountUseCaseListener.onFinishUseCase();
	// }
	// }
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// executeOnUIThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// ToastUtils.showInfoToast(facebookConnector.isConnected() ? R.string.accountConnected
		// : R.string.accountDisconnected);
		// dismissLoading();
		// }
		// });
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		// connectToFacebookUseCase.setAccessToken(facebookConnector.connectCallback(requestCode, resultCode, data));
		// executeUseCase(connectToFacebookUseCase);
	}
}
