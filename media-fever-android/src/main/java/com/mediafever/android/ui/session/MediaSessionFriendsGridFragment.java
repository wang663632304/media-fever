package com.mediafever.android.ui.session;

import java.util.List;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.java.collections.Lists;
import com.mediafever.R;
import com.mediafever.android.ui.UserAdapter;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.session.MediaSessionUser;
import com.mediafever.usecase.friends.FriendsUseCase;
import com.mediafever.usecase.mediasession.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MediaSessionFriendsGridFragment extends AbstractGridFragment<UserImpl> {
	
	private FriendsUseCase friendsUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		friendsUseCase = getInstance(FriendsUseCase.class);
		friendsUseCase.setUserId(getUser().getId());
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.media_session_friends_grid_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		loadFriends();
		getGridView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
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
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onGridItemClick(android.widget.GridView, android.view.View,
	 *      int, long)
	 */
	@Override
	public void onGridItemClick(GridView parent, View view, int position, long id) {
		
		UserImpl user = (UserImpl)parent.getAdapter().getItem(position);
		if (parent.isItemChecked(position)) {
			getMediaSessionSetupUseCase().addUser(user);
		} else {
			getMediaSessionSetupUseCase().removeUser(user);
		}
	}
	
	private void loadFriends() {
		setListAdapter(new UserAdapter(MediaSessionFriendsGridFragment.this.getActivity(), friendsUseCase.getFriends()));
		MediaSession mediaSession = getMediaSessionSetupUseCase().getMediaSession();
		if (mediaSession.getId() != null) {
			List<User> mediaSessionUsers = Lists.newArrayList();
			for (MediaSessionUser each : mediaSession.getUsers()) {
				mediaSessionUsers.add(each.getUser());
			}
			List<UserImpl> friends = friendsUseCase.getFriends();
			for (int i = 0; i < friends.size(); i++) {
				User friend = friends.get(i);
				if (mediaSessionUsers.contains(friend)) {
					getGridView().setItemChecked(i, true);
				}
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
