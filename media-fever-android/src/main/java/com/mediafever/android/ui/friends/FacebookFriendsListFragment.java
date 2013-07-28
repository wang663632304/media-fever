package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.AdSize;
import com.jdroid.android.fragment.AbstractListFragment;
import com.mediafever.R;
import com.mediafever.domain.SocialUser;
import com.mediafever.usecase.friends.FacebookFriendsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookFriendsListFragment extends AbstractListFragment<SocialUser> implements FacebookFriendsListener {
	
	private FacebookFriendsUseCase facebookFriendsUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle(R.string.friends);
		
		facebookFriendsUseCase = getInstance(FacebookFriendsUseCase.class);
		facebookFriendsUseCase.setUserId(getUser().getId());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.facebook_friends_list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(facebookFriendsUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(facebookFriendsUseCase, this);
	}
	
	/**
	 * @see com.mediafever.android.ui.friends.FacebookFriendsListener#loadFriends()
	 */
	@Override
	public void loadFriends() {
		executeUseCase(facebookFriendsUseCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				findView(R.id.facebookLoginContainer).setVisibility(View.GONE);
				setListAdapter(new FacebookUserAdapter(getActivity(), facebookFriendsUseCase.getFriends()));
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(SocialUser user) {
		CreateFriendRequestDialogFragment.show(getActivity(), user.getId(), user.getFullname());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return null;
	}
	
}
