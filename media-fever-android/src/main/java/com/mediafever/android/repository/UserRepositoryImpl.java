package com.mediafever.android.repository;

import com.jdroid.android.domain.User;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.utils.SharedPreferencesUtils;
import com.mediafever.domain.UserImpl;

/**
 * {@link UserRepository} implementation that saves and loads the user from the Android shared preferences
 * 
 * @author Maxi Rosson
 */
public class UserRepositoryImpl implements UserRepository {
	
	private static final String USER_ID = "user.id";
	private static final String USER_TOKEN = "user.token";
	private static final String USER_EMAIL = "user.email";
	private static final String USER_FIRST_NAME = "user.firstname";
	private static final String USER_LAST_NAME = "user.lastname";
	private static final String USER_IMAGE = "user.image";
	private static final String PUBLIC_PROFILE = "user.publicProfile";
	
	/**
	 * @see com.jdroid.android.repository.UserRepository#getUser()
	 */
	@Override
	public UserImpl getUser() {
		
		Long id = SharedPreferencesUtils.loadPreferenceAsLong(USER_ID);
		if (id != null) {
			String userToken = SharedPreferencesUtils.loadPreference(USER_TOKEN);
			String firstname = SharedPreferencesUtils.loadPreference(USER_FIRST_NAME);
			String lastname = SharedPreferencesUtils.loadPreference(USER_LAST_NAME);
			String email = SharedPreferencesUtils.loadPreference(USER_EMAIL);
			String image = SharedPreferencesUtils.loadPreference(USER_IMAGE);
			Boolean publicProfile = SharedPreferencesUtils.loadPreferenceAsBoolean(PUBLIC_PROFILE);
			return new UserImpl(id, userToken, email, firstname, lastname, image, publicProfile);
		}
		return null;
	}
	
	/**
	 * @see com.jdroid.android.repository.UserRepository#saveUser(com.jdroid.android.domain.User)
	 */
	@Override
	public void saveUser(User user) {
		SharedPreferencesUtils.savePreference(USER_ID, user.getId());
		SharedPreferencesUtils.savePreference(USER_TOKEN, user.getUserToken());
		SharedPreferencesUtils.savePreference(USER_EMAIL, user.getEmail());
		SharedPreferencesUtils.savePreference(USER_FIRST_NAME, user.getFirstName());
		SharedPreferencesUtils.savePreference(USER_LAST_NAME, user.getLastName());
		SharedPreferencesUtils.savePreference(USER_IMAGE, user.getImage().getUriAsString());
		SharedPreferencesUtils.savePreference(PUBLIC_PROFILE, ((UserImpl)user).hasPublicProfile());
	}
	
	/**
	 * @see com.jdroid.android.repository.UserRepository#removeUser()
	 */
	@Override
	public void removeUser() {
		SharedPreferencesUtils.removePreferences(USER_ID, USER_TOKEN, USER_EMAIL, USER_FIRST_NAME, USER_LAST_NAME,
			USER_IMAGE, PUBLIC_PROFILE);
	}
}
