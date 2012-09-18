package com.mediafever.usecase;

import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.domain.User;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.android.AndroidErrorCode;
import com.mediafever.domain.UserImpl;
import com.mediafever.service.APIService;

/**
 * Use case that handles users editions.
 * 
 * @author Maxi Rosson
 */
public class UpdateUserProfileUseCase extends AbstractApiUseCase<APIService> {
	
	private String firstName;
	private String lastName;
	private FileContent avatar;
	private String email;
	private String confirmEmail;
	private Boolean changePassword;
	private String password;
	private String confirmPassword;
	private Boolean publicProfile;
	
	/**
	 * @param apiService
	 */
	@Inject
	public UpdateUserProfileUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		
		// TODO These validations are duplicated on user signup usecase. We should unify them
		AndroidErrorCode.REQUIRED_FIRST_NAME.validateRequired(firstName);
		AndroidErrorCode.REQUIRED_LAST_NAME.validateRequired(lastName);
		
		AndroidErrorCode.REQUIRED_EMAIL.validateRequired(email);
		AndroidErrorCode.INVALID_EMAIL.validateEmail(email);
		AndroidErrorCode.REQUIRED_CONFIRM_EMAIL.validateRequired(confirmEmail);
		AndroidErrorCode.EMAILS_DONT_MATCH.validateEquals(email, confirmEmail);
		
		if (changePassword) {
			AndroidErrorCode.REQUIRED_PASSWORD.validateRequired(password);
			AndroidErrorCode.INVALID_PASSWORD.validateMinimumLength(password, 5);
			AndroidErrorCode.REQUIRED_CONFIRM_PASSWORD.validateRequired(confirmPassword);
			AndroidErrorCode.PASSWORDS_DONT_MATCH.validateEquals(password, confirmPassword);
		} else {
			password = null;
		}
		
		User user = SecurityContext.get().getUser();
		User editedUser = getApiService().editUser(user.getId(),
			new UserImpl(email, password, firstName, lastName, null, publicProfile), avatar);
		
		((UserImpl)user).modify(email, password, firstName, lastName, editedUser.getImage().getUriAsString(),
			publicProfile);
		SecurityContext.get().attach(user);
		avatar = null;
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
	
	public void setPublicProfile(Boolean publicProfile) {
		this.publicProfile = publicProfile;
	}
	
	public void setChangePassword(Boolean changePassword) {
		this.changePassword = changePassword;
	}
	
	public void setAvatar(FileContent avatar) {
		this.avatar = avatar;
	}
}
