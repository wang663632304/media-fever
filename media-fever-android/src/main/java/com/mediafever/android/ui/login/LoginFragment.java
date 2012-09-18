package com.mediafever.android.ui.login;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.listener.LaunchOnClickListener;
import com.jdroid.android.utils.AndroidUtils;
import com.google.ads.AdSize;
import com.google.android.gcm.GCMRegistrar;
import com.mediafever.R;
import com.mediafever.android.AndroidApplication;
import com.mediafever.android.ui.listener.BuyFullAppOnClickListener;
import com.mediafever.android.ui.signup.SignUpActivity;
import com.mediafever.context.ApplicationContext;
import com.mediafever.usecase.LoginUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class LoginFragment extends AbstractFragment {
	
	private LoginUseCase loginUseCase;
	
	@InjectView(R.id.email)
	private TextView email;
	
	@InjectView(R.id.password)
	private TextView password;
	
	@InjectView(R.id.login)
	private Button login;
	
	@InjectView(R.id.signUp)
	private Button signUp;
	
	@InjectView(R.id.buyFullApp)
	private Button buyFullApp;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.login_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? AdSize.IAB_LEADERBOARD : AdSize.SMART_BANNER;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// TODO Hardcoded login
		email.setText("user1@email.com");
		password.setText("admin");
		
		if (ApplicationContext.get().isFreeApp()) {
			
			buyFullApp.setVisibility(View.VISIBLE);
			buyFullApp.setOnClickListener(new BuyFullAppOnClickListener());
		} else {
			buyFullApp.setVisibility(View.GONE);
		}
		
		if (loginUseCase == null) {
			loginUseCase = getInstance(LoginUseCase.class);
		}
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginUseCase.setEmail(email.getText().toString());
				loginUseCase.setPassword(password.getText().toString());
				executeUseCase(loginUseCase);
			}
		});
		
		signUp.setOnClickListener(new LaunchOnClickListener(SignUpActivity.class));
		
		if (AndroidApplication.get().getLoginRunnable() != null) {
			AndroidApplication.get().getLoginRunnable().run();
			AndroidApplication.get().setLoginRunnable(null);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(loginUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(loginUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				GCMRegistrar.register(AndroidApplication.get(), ApplicationContext.get().getGoogleProjectId());
				getActivity().finish();
				dismissLoading();
				ActivityLauncher.launchHomeActivity();
			}
		});
	}
}