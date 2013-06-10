package com.mediafever.android.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jdroid.android.AbstractApplication;
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
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle(R.string.signUp);
		signUpUseCase = getInstance(SignUpUseCase.class);
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
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		final TextView firstName = findView(R.id.firstName);
		final TextView lastName = findView(R.id.lastName);
		final TextView email = findView(R.id.email);
		final TextView confirmEmail = findView(R.id.confirmEmail);
		final TextView password = findView(R.id.password);
		final TextView confirmPassword = findView(R.id.confirmPassword);
		
		findView(R.id.signUp).setOnClickListener(new OnClickListener() {
			
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
	 * @see com.jdroid.android.fragment.AbstractFragment#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return false;
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
				ActivityLauncher.launchActivityClearTask(AbstractApplication.get().getHomeActivityClass(), true);
			}
		});
	}
}
