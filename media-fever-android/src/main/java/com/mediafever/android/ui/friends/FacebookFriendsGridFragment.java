package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.ads.AdSize;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.facebook.FacebookPreferencesUtils;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.mediafever.R;
import com.mediafever.domain.SocialUser;
import com.mediafever.usecase.friends.FacebookFriendsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookFriendsGridFragment extends AbstractGridFragment<SocialUser> implements FacebookFriendsListener {
	
	private FacebookFriendsUseCase facebookFriendsUseCase;
	
	private View facebookLoginContainer;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle(R.string.friends);
		
		facebookFriendsUseCase = getInstance(FacebookFriendsUseCase.class);
		facebookFriendsUseCase.setUserId(getUser().getId());
		
		if (getFacebookFriendsHelperFragment() == null) {
			((AbstractFragmentActivity)getActivity()).addFragment(new FacebookFriendsHelperFragment(), 0, false);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.facebook_friends_grid_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		facebookLoginContainer = findView(R.id.facebookLoginContainer);
		if (FacebookPreferencesUtils.existsFacebookAccessToken()) {
			facebookLoginContainer.setVisibility(View.GONE);
		} else {
			findView(android.R.id.empty).setVisibility(View.GONE);
			findView(R.id.authButton).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getFacebookFriendsHelperFragment().startLoginProcess();
				}
			});
			facebookLoginContainer.setVisibility(View.VISIBLE);
		}
	}
	
	private FacebookFriendsHelperFragment getFacebookFriendsHelperFragment() {
		return ((AbstractFragmentActivity)getActivity()).getFragment(FacebookFriendsHelperFragment.class);
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
				facebookLoginContainer.setVisibility(View.GONE);
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
