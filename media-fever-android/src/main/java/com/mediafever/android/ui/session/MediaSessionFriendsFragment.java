package com.mediafever.android.ui.session;

import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.AbstractListFragment;
import com.mediafever.R;
import com.mediafever.android.ui.UserCheckeableAdapter;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.usecase.friends.FriendsUseCase;
import com.mediafever.usecase.mediasession.MediaSessionSetupUseCase;

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
		setListAdapter(new MediaSessionUsersAdapter(MediaSessionFriendsFragment.this.getActivity(),
				friendsUseCase.getFriends()));
	}
	
	private class MediaSessionUsersAdapter extends UserCheckeableAdapter {
		
		private List<User> originalUsers;
		
		public MediaSessionUsersAdapter(Activity context, List<UserImpl> items) {
			super(context, items);
			originalUsers = getMediaSessionSetupUseCase().getMediaSession().getUsers();
		}
		
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
		
		@Override
		protected Boolean isUserEnabled(UserImpl user) {
			MediaSession mediaSession = getMediaSessionSetupUseCase().getMediaSession();
			if (mediaSession.getId() != null) {
				return !originalUsers.contains(user);
			} else {
				return true;
			}
		}
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
