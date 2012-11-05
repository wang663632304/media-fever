package com.mediafever.usecase.settings;

import android.content.Context;
import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.facebook.FacebookConnector;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.service.APIService;

/**
 * Use case to connect/disconnect our account to a Facebook account.
 * 
 * @author Estefanía Caravatti
 */
public class ConnectToFacebookUseCase extends AbstractApiUseCase<APIService> {
	
	private String accessToken;
	private String facebookUserId;
	private Boolean connected;
	private FacebookConnector facebookConnector;
	private Context context;
	
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
		if (connected) {
			facebookConnector.disconnect(context);
			connected = false;
		} else {
			facebookUserId = facebookConnector.getFacebookUserId();
			getApiService().connectToFacebook(SecurityContext.get().getUser().getId(), facebookUserId, accessToken,
				facebookConnector.getAccessExpires());
			connected = true;
		}
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
	
	/**
	 * @param facebookConnector the facebookConnector to set
	 */
	public void setFacebookConnector(FacebookConnector facebookConnector) {
		this.facebookConnector = facebookConnector;
	}
	
	/**
	 * @param connected the isConnected to set
	 */
	public void setConnected(Boolean connected) {
		this.connected = connected;
	}
	
	/**
	 * @return whether it is connected to Facebook.
	 */
	public Boolean isConnected() {
		return connected;
	}
	
	/**
	 * @param context the context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}
}
