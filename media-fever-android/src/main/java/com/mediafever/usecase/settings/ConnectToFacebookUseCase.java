package com.mediafever.usecase.settings;

import java.util.Date;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.service.APIService;

/**
 * Use case to connect/disconnect our account to a Facebook account.
 * 
 * @author Estefanía Caravatti
 */
public class ConnectToFacebookUseCase extends AbstractApiUseCase<APIService> {
	
	private String accessToken;
	private Date expirationDate;
	private Boolean connect;
	
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
		
		if (connect) {
			Response response = Request.executeAndWait(Request.newMeRequest(Session.getActiveSession(), null));
			GraphUser user = response.getGraphObjectAs(GraphUser.class);
			getApiService().connectToFacebook(SecurityContext.get().getUser().getId(), user.getId(), accessToken,
				expirationDate);
			connect = Boolean.FALSE;
		} else {
			getApiService().disconnectFromFacebook(SecurityContext.get().getUser().getId());
		}
	}
	
	public void setDataToConnect(String accessToken, Date expirationDate) {
		connect = Boolean.TRUE;
		this.accessToken = accessToken;
		this.expirationDate = expirationDate;
	}
	
}
