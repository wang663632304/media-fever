package com.mediafever.usecase.settings;

import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.social.FacebookAccount;
import com.mediafever.service.APIService;

/**
 * Use case to get the user's {@link FacebookAccount}.
 * 
 * @author Estefanía Caravatti
 */
public class FacebookAccountUseCase extends AbstractApiUseCase<APIService> {
	
	private FacebookAccount facebookAccount;
	
	/**
	 * @param apiService
	 */
	@Inject
	public FacebookAccountUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.AbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		facebookAccount = getApiService().getFacebookAccount(SecurityContext.get().getUser().getId());
	}
	
	/**
	 * @return the facebookAccount
	 */
	public FacebookAccount getFacebookAccount() {
		return facebookAccount;
	}
}
