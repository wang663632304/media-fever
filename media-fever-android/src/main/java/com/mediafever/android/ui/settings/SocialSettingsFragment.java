package com.mediafever.android.ui.settings;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.jdroid.android.facebook.FacebookConnector;
import com.jdroid.android.fragment.AbstractFragment;
import com.mediafever.R;
import com.mediafever.context.ApplicationContext;
import com.mediafever.usecase.settings.ConnectToFacebookUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialSettingsFragment extends AbstractFragment {
	
	private ConnectToFacebookUseCase connectToFacebookUseCase;
	
	private FacebookConnector facebookConnector;
	
	@InjectView(R.id.connectToFacebook)
	private ImageButton connectButton;
	
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
		
		if (connectToFacebookUseCase == null) {
			connectToFacebookUseCase = getInstance(ConnectToFacebookUseCase.class);
			connectToFacebookUseCase.addListener(this);
		}
		
		facebookConnector = new FacebookConnector(ApplicationContext.get().getFacebookAppId());
		
		connectButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				facebookConnector.connect(getActivity());
			}
		});
		
		if (connectToFacebookUseCase.isInProgress()) {
			onStartUseCase();
		} else if (connectToFacebookUseCase.isFinish()) {
			onFinishUseCase();
		}
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
