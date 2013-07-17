package com.mediafever.android.ui.settings;

import com.jdroid.android.facebook.BasicFacebookUserInfo;
import com.jdroid.android.facebook.FacebookHelperFragment;
import com.jdroid.android.facebook.FacebookLoginUseCase;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.usecase.settings.MediaFeverFacebookLoginUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SocialSettingsHelperFragment extends FacebookHelperFragment {
	
	/**
	 * @see com.jdroid.android.facebook.FacebookHelperFragment#createFacebookLoginUseCase()
	 */
	@Override
	protected FacebookLoginUseCase createFacebookLoginUseCase() {
		return getInstance(MediaFeverFacebookLoginUseCase.class);
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookHelperFragment#onFacebookLoginUseCaseFinished(com.jdroid.android.facebook.BasicFacebookUserInfo)
	 */
	@Override
	protected void onFacebookLoginUseCaseFinished(BasicFacebookUserInfo userInfo) {
		LoggerUtils.getLogger(SocialSettingsHelperFragment.class).debug("Login !!!!!!");
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookHelperFragment#onFacebookLogoutUseCaseFinised()
	 */
	@Override
	protected void onFacebookLogoutUseCaseFinised() {
		LoggerUtils.getLogger(SocialSettingsHelperFragment.class).debug("Logout !!!!!!");
	}
}
