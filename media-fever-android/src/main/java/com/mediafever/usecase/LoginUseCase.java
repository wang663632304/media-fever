package com.mediafever.usecase;

import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.android.AndroidErrorCode;
import com.mediafever.domain.UserImpl;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class LoginUseCase extends AbstractApiUseCase<APIService> {
	
	private String email;
	private String password;
	
	@Inject
	public LoginUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		AndroidErrorCode.REQUIRED_EMAIL.validateRequired(email);
		AndroidErrorCode.REQUIRED_PASSWORD.validateRequired(password);
		UserImpl user = getApiService().login(email, password);
		if (user != null) {
			SecurityContext.get().attach(user);
		} else {
			throw AndroidErrorCode.INVALID_CREDENTIALS.newBusinessException();
		}
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
