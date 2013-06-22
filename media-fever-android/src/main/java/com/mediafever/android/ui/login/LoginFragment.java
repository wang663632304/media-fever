package com.mediafever.android.ui.login;

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
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loginUseCase = getInstance(LoginUseCase.class);
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
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// TODO Hardcoded login
		final TextView email = findView(R.id.email);
		email.setText("user1@email.com");
		
		final TextView password = findView(R.id.password);
		password.setText("admin");
		
		Button buyFullApp = findView(R.id.buyFullApp);
		if (ApplicationContext.get().isFreeApp()) {
			buyFullApp.setVisibility(View.VISIBLE);
			buyFullApp.setOnClickListener(new BuyFullAppOnClickListener());
		} else {
			buyFullApp.setVisibility(View.GONE);
		}
		
		findView(R.id.login).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginUseCase.setEmail(email.getText().toString());
				loginUseCase.setPassword(password.getText().toString());
				executeUseCase(loginUseCase);
			}
		});
		
		findView(R.id.signUp).setOnClickListener(new LaunchOnClickListener(SignUpActivity.class));
		
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
	 * @see com.jdroid.android.fragment.AbstractFragment#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return false;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				getActivity().finish();
				dismissLoading();
				ActivityLauncher.launchHomeActivity();
			}
		});
	}
}