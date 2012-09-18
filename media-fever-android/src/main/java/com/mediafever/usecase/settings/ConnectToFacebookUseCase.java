package com.mediafever.usecase.settings;

import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.service.APIService;

/**
 * Use case to connect our account to a Facebook account.
 * 
 * @author Estefanía Caravatti
 */
public class ConnectToFacebookUseCase extends AbstractApiUseCase<APIService> {
	
	private String accessToken;
	private String facebookUserId;
	
	/**
	 * @param apiService
	 */
	@Inject
	public ConnectToFacebookUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		getApiService().connectToFacebook(SecurityContext.get().getUser().getId(), facebookUserId, accessToken);
	}
	
	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	/**
	 * @param facebookUserId the facebookUserId to set
	 */
	public void setFacebookUserId(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}
}
