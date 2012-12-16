package com.mediafever.android.ui.session;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.mediafever.R;
import com.mediafever.android.ui.UserAdapter;
import com.mediafever.domain.UserImpl;
import com.mediafever.usecase.FriendsUseCase;
import com.mediafever.usecase.MediaSessionSetupUseCase;

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
		
		if (friendsUseCase == null) {
			friendsUseCase = getInstance(FriendsUseCase.class);
			friendsUseCase.setUserId(getUser().getId());
		}
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
	 * @see com.jdroid.android.fragment.AbstractFragment#onActivityCreated(android.os.Bundle)
	 */
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadFriends();
		getGridView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
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
