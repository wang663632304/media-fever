package com.mediafever.android.ui.friends;

import com.jdroid.android.facebook.BasicFacebookUserInfo;
import com.jdroid.android.facebook.FacebookHelperFragment;
import com.mediafever.usecase.settings.MediaFeverFacebookLoginUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookFriendsHelperFragment extends FacebookHelperFragment<MediaFeverFacebookLoginUseCase> {
	
	/**
	 * @see com.jdroid.android.facebook.FacebookHelperFragment#createFacebookLoginUseCase()
	 */
	@Override
	protected MediaFeverFacebookLoginUseCase createFacebookLoginUseCase() {
		return getInstance(MediaFeverFacebookLoginUseCase.class);
	}
	
	/**
	 * @see com.jdroid.android.facebook.FacebookHelperFragment#onFacebookLoginUseCaseFinished(com.jdroid.android.facebook.BasicFacebookUserInfo)
	 */
	@Override
	protected void onFacebookLoginUseCaseFinished(BasicFacebookUserInfo userInfo) {
		
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
