package com.mediafever.usecase.settings;

import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.facebook.FacebookAuthenticationUseCase;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaFeverFacebookAuthenticationUseCase extends FacebookAuthenticationUseCase {
	
	private APIService apiService;
	
	@Inject
	public MediaFeverFacebookAuthenticationUseCase(APIService apiService) {
		this.apiService = apiService;
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationUseCase#sendFacebookLogin(java.lang.String, java.lang.String)
	 */
	@Override
	protected void sendFacebookLogin(String facebookId, String token) {
		apiService.connectToFacebook(SecurityContext.get().getUser().getId(), facebookId, token);
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationUseCase#afterFacebookLogout()
	 */
	@Override
	protected void afterFacebookLogout() {
		apiService.disconnectFromFacebook(SecurityContext.get().getUser().getId());
	}
}
