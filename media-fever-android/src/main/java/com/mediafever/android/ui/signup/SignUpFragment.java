package com.mediafever.android.ui.signup;

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
import com.mediafever.R;
import com.mediafever.usecase.SignUpUseCase;

/**
 * Fragment that handles new users' sign up.
 * 
 * @author Estefanía Caravatti
 */
public class SignUpFragment extends AbstractFragment {
	
	private SignUpUseCase signUpUseCase;
	
	@InjectView(R.id.firstName)
	private TextView firstName;
	
	@InjectView(R.id.lastName)
	private TextView lastName;
	
	@InjectView(R.id.email)
	private TextView email;
	
	@InjectView(R.id.confirmEmail)
	private TextView confirmEmail;
	
	@InjectView(R.id.password)
	private TextView password;
	
	@InjectView(R.id.confirmPassword)
	private TextView confirmPassword;
	
	@InjectView(R.id.signUp)
	private Button signUp;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		getSupportActionBar().setTitle(R.string.signUp);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.signup_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (signUpUseCase == null) {
			signUpUseCase = getInstance(SignUpUseCase.class);
		}
		
		signUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				signUpUseCase.setFirstName(firstName.getText().toString());
				signUpUseCase.setLastName(lastName.getText().toString());
				signUpUseCase.setEmail(email.getText().toString());
				signUpUseCase.setConfirmEmail(confirmEmail.getText().toString());
				signUpUseCase.setPassword(password.getText().toString());
				signUpUseCase.setConfirmPassword(confirmPassword.getText().toString());
				executeUseCase(signUpUseCase);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(signUpUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(signUpUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
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
