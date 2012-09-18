package com.mediafever.usecase;

import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.android.AndroidErrorCode;
import com.mediafever.domain.UserImpl;
import com.mediafever.service.APIService;

/**
 * Use case that handles new users' sign up.
 * 
 * @author Estefanía Caravatti
 */
public class SignUpUseCase extends AbstractApiUseCase<APIService> {
	
	private String firstName;
	private String lastName;
	private String email;
	private String confirmEmail;
	private String password;
	private String confirmPassword;
	
	/**
	 * @param apiService
	 */
	@Inject
	public SignUpUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		
		// TODO These validations are duplicated on user update user profile usecase. We should unify them
		AndroidErrorCode.REQUIRED_FIRST_NAME.validateRequired(firstName);
		AndroidErrorCode.REQUIRED_LAST_NAME.validateRequired(lastName);
		
		AndroidErrorCode.REQUIRED_EMAIL.validateRequired(email);
		AndroidErrorCode.INVALID_EMAIL.validateEmail(email);
		AndroidErrorCode.REQUIRED_CONFIRM_EMAIL.validateRequired(confirmEmail);
		AndroidErrorCode.EMAILS_DONT_MATCH.validateEquals(email, confirmEmail);
		
		AndroidErrorCode.REQUIRED_PASSWORD.validateRequired(password);
		AndroidErrorCode.INVALID_PASSWORD.validateMinimumLength(password, 5);
		AndroidErrorCode.REQUIRED_CONFIRM_PASSWORD.validateRequired(confirmPassword);
		AndroidErrorCode.PASSWORDS_DONT_MATCH.validateEquals(password, confirmPassword);
		
		UserImpl user = getApiService().signup(new UserImpl(email, password, firstName, lastName, null, null));
		
		if (user != null) {
			SecurityContext.get().attach(user);
		}
	}
	
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @param confirmEmail the confirmEmail to set
	 */
	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}
	
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
