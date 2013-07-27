package com.mediafever.android.ui.friends;

import com.jdroid.android.facebook.FacebookAuthenticationFragment;
import com.jdroid.android.facebook.FacebookUser;
import com.mediafever.usecase.settings.MediaFeverFacebookAuthenticationUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookFriendsHelperFragment extends
		FacebookAuthenticationFragment<MediaFeverFacebookAuthenticationUseCase> {
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationFragment#createFacebookAuthenticationUseCase()
	 */
	@Override
	protected MediaFeverFacebookAuthenticationUseCase createFacebookAuthenticationUseCase() {
		return getInstance(MediaFeverFacebookAuthenticationUseCase.class);
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookAuthenticationFragment#onFacebookLoginUseCaseFinished(com.jdroid.android.facebook.FacebookUser)
	 */
	@Override
	protected void onFacebookLoginUseCaseFinished(FacebookUser facebookUser) {
		
		// TODO Use FacebookFriendsListener
		FacebookFriendsListFragment fragment = (FacebookFriendsListFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag(
			FacebookFriendsListFragment.class.getSimpleName());
		if (fragment != null) {
			fragment.loadFriends();
		}
		
		FacebookFriendsGridFragment fragment2 = (FacebookFriendsGridFragment)getSherlockActivity().getSupportFragmentManager().findFragmentByTag(
			FriendsContextualItem.FACEBOOK.getName());
		if (fragment2 != null) {
			fragment2.loadFriends();
		}
	}
	
}
