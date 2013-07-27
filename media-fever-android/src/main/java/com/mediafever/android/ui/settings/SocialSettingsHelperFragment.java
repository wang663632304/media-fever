package com.mediafever.android.ui.settings;

import com.jdroid.android.facebook.FacebookAuthenticationFragment;
import com.jdroid.android.facebook.FacebookUser;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.usecase.settings.MediaFeverFacebookAuthenticationUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialSettingsHelperFragment extends
		FacebookAuthenticationFragment<MediaFeverFacebookAuthenticationUseCase> {
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationFragment#createFacebookAuthenticationUseCase()
	 */
	@Override
	protected MediaFeverFacebookAuthenticationUseCase createFacebookAuthenticationUseCase() {
		return getInstance(MediaFeverFacebookAuthenticationUseCase.class);
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationFragment#onFacebookLoginUseCaseFinished(com.jdroid.android.facebook.FacebookUser)
	 */
	@Override
	protected void onFacebookLoginUseCaseFinished(FacebookUser facebookUser) {
		// TODO Implement this
		LoggerUtils.getLogger(SocialSettingsHelperFragment.class).debug("Login !!!!!!");
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationFragment#onFacebookLogoutUseCaseFinised()
	 */
	@Override
	protected void onFacebookLogoutUseCaseFinised() {
		// TODO Implement this
		LoggerUtils.getLogger(SocialSettingsHelperFragment.class).debug("Logout !!!!!!");
	}
}
