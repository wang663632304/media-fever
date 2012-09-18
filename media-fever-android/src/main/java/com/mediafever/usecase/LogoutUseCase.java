package com.mediafever.usecase;

import com.jdroid.android.usecase.AbstractApiUseCase;
import com.jdroid.android.utils.AndroidUtils;
import com.google.inject.Inject;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class LogoutUseCase extends AbstractApiUseCase<APIService> {
	
	private String userToken;
	
	@Inject
	public LogoutUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		getApiService().disableDevice(AndroidUtils.getInstallationId(), userToken);
	}
	
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
