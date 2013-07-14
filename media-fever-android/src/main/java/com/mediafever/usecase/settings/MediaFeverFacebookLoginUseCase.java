package com.mediafever.usecase.settings;

import java.util.Date;
import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.facebook.FacebookLoginUseCase;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaFeverFacebookLoginUseCase extends FacebookLoginUseCase {
	
	private APIService apiService;
	
	@Inject
	public MediaFeverFacebookLoginUseCase(APIService apiService) {
		this.apiService = apiService;
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookLoginUseCase#sendFacebookLogin(java.lang.String, java.lang.String)
	 */
	@Override
	protected void sendFacebookLogin(String facebookId, String token) {
		
		// TODO See if we should send the access expiration date
		apiService.connectToFacebook(SecurityContext.get().getUser().getId(), facebookId, token, new Date());
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookLoginUseCase#sendFacebookLogout()
	 */
	@Override
	protected void sendFacebookLogout() {
		apiService.disconnectFromFacebook(SecurityContext.get().getUser().getId());
	}
}
