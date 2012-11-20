package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractListFragment;
import com.mediafever.R;
import com.mediafever.android.ui.UserCheckeableAdapter;
import com.mediafever.domain.UserImpl;
import com.mediafever.usecase.FriendsUseCase;
import com.mediafever.usecase.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionFriendsFragment extends AbstractListFragment<UserImpl> {
	
	private FriendsUseCase friendsUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		if (friendsUseCase == null) {
			friendsUseCase = getInstance(FriendsUseCase.class);
			friendsUseCase.setUserId(getUser().getId());
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().addHeaderView(
			LayoutInflater.from(getActivity()).inflate(R.layout.media_session_friends_header_fragment, null));
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(friendsUseCase, this, true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(friendsUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#getNoResultsText()
	 */
	@Override
	protected int getNoResultsText() {
		return R.string.noResultsFriends;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(new UserCheckeableAdapter(MediaSessionFriendsFragment.this.getActivity(),
						friendsUseCase.getFriends()) {
					
					@Override
					protected void onUserChecked(UserImpl user) {
						getMediaSessionSetupUseCase().addUser(user);
					}
					
					@Override
					protected void onUserUnChecked(UserImpl user) {
						getMediaSessionSetupUseCase().removeUser(user);
					}
					
					@Override
					protected Boolean isUserChecked(UserImpl user) {
						return getMediaSessionSetupUseCase().containsUser(user);
					}
				});
				dismissLoading();
			}
		});
	}
	
	public MediaSessionSetupUseCase getMediaSessionSetupUseCase() {
		return ((MediaSessionActivity)getActivity()).getMediaSessionSetupUseCase();
	}
}
