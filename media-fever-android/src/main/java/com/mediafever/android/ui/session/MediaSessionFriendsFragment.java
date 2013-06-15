package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.fragment.AbstractListFragment;
import com.mediafever.R;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.friends.FriendsUseCase;
import com.mediafever.usecase.mediasession.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionFriendsFragment extends AbstractListFragment<UserImpl> {
	
	private MediaSession mediaSession;
	private FriendsUseCase friendsUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mediaSession = ((MediaSessionActivity)getActivity()).getMediaSessionSetupUseCase().getMediaSession();
		
		friendsUseCase = getInstance(FriendsUseCase.class);
		friendsUseCase.setUserId(getUser().getId());
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
		loadFriends();
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(friendsUseCase, this, UseCaseTrigger.ONCE);
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
				loadFriends();
				dismissLoading();
			}
			
		});
	}
	
	private void loadFriends() {
		setListAdapter(new MediaSessionUserAdapter(MediaSessionFriendsFragment.this.getActivity(),
				friendsUseCase.getFriends(), mediaSession) {
			
			@Override
			protected Boolean isUserEnabled(UserImpl user) {
				return getMediaSessionSetupUseCase().isRemovableUser(user);
			}
			
			@Override
			protected Boolean isAcceptedUser(UserImpl user) {
				return getMediaSessionSetupUseCase().isAcceptedUser(user);
			}
			
			@Override
			protected Boolean isPendingUser(UserImpl user) {
				return getMediaSessionSetupUseCase().isPendingUser(user);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}
	
	public MediaSessionSetupUseCase getMediaSessionSetupUseCase() {
		return ((MediaSessionActivity)getActivity()).getMediaSessionSetupUseCase();
	}
}
