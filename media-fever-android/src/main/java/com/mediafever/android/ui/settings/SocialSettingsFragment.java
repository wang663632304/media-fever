package com.mediafever.android.ui.settings;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.jdroid.android.AndroidUseCaseListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.facebook.FacebookConnector;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.utils.ToastUtils;
import com.mediafever.R;
import com.mediafever.context.ApplicationContext;
import com.mediafever.domain.social.FacebookAccount;
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
	
	private FacebookConnector facebookConnector = new FacebookConnector(ApplicationContext.get().getFacebookAppId());;
	
	@InjectView(R.id.connectToFacebook)
	private CompoundButton connectButton;
	
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
		setRetainInstance(true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initFacebookAccountUseCase();
		initConnectToFacebookUseCase();
		
		connectButton.setChecked(facebookConnector.isConnected());
		
		connectButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Boolean isConnected = facebookConnector.isConnected();
				connectToFacebookUseCase.setConnect(isChecked);
				
				// Connect to Facebook only if the button has been checked and we don't have a connection already.
				if (isChecked && !isConnected) {
					facebookConnector.connect(getActivity());
				} else if (!isChecked && isConnected) {
					// Disconnect from Facebook only when we are already connected and the button has been unchecked.
					connectToFacebookUseCase.setContext(getActivity());
					executeUseCase(connectToFacebookUseCase);
				}
			}
		});
		
	}
	
	/**
	 * 
	 */
	private void initConnectToFacebookUseCase() {
		if (connectToFacebookUseCase == null) {
			connectToFacebookUseCase = getInstance(ConnectToFacebookUseCase.class);
			connectToFacebookUseCase.addListener(this);
			connectToFacebookUseCase.setFacebookConnector(facebookConnector);
		} else if (connectToFacebookUseCase.isInProgress()) {
			onStartUseCase();
		} else if (connectToFacebookUseCase.isFinish()) {
			onFinishUseCase();
		}
	}
	
	private void initFacebookAccountUseCase() {
		if (facebookAccountUseCase == null) {
			facebookAccountUseCase = getInstance(FacebookAccountUseCase.class);
			facebookAccountUseCaseListener = new AndroidUseCaseListener() {
				
				@Override
				public void onFinishUseCase() {
					executeOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							FacebookAccount facebookAccount = facebookAccountUseCase.getFacebookAccount();
							if (facebookAccount != null) {
								facebookConnector.setAccessToken(facebookAccount.getAccessToken(),
									facebookAccount.getAccessExpiresIn());
								connectButton.setChecked(facebookConnector.isConnected());
							}
							dismissLoading();
						}
					});
				}
				
				@Override
				protected ActivityIf getActivityIf() {
					return (ActivityIf)getActivity();
				}
			};
			
			facebookAccountUseCase.addListener(facebookAccountUseCaseListener);
			executeUseCase(facebookAccountUseCase);
			
		} else if (facebookAccountUseCase.isInProgress()) {
			facebookAccountUseCaseListener.onStartUseCase();
		} else if (facebookAccountUseCase.isFinish()) {
			facebookAccountUseCaseListener.onFinishUseCase();
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
				ToastUtils.showInfoToast(facebookConnector.isConnected() ? R.string.accountConnected
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
		connectToFacebookUseCase.setAccessToken(facebookConnector.connectCallback(requestCode, resultCode, data));
		executeUseCase(connectToFacebookUseCase);
	}
}
