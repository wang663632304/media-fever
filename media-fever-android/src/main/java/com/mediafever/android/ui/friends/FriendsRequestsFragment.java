package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.mediafever.R;
import com.mediafever.domain.FriendRequest;
import com.mediafever.usecase.friends.AcceptFriendRequestUseCase;
import com.mediafever.usecase.friends.FriendRequestsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendsRequestsFragment extends AbstractListFragment<FriendRequest> {
	
	private FriendRequestsUseCase friendRequestsUseCase;
	private AcceptFriendRequestUseCase acceptFriendRequestUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle(R.string.friendsRequests);
		
		friendRequestsUseCase = getInstance(FriendRequestsUseCase.class);
		friendRequestsUseCase.setUserId(getUser().getId());
		
		acceptFriendRequestUseCase = getInstance(AcceptFriendRequestUseCase.class);
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
		
		getListView().setItemsCanFocus(true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(friendRequestsUseCase, this, UseCaseTrigger.ONCE);
		onResumeUseCase(acceptFriendRequestUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(friendRequestsUseCase, this);
		onPauseUseCase(acceptFriendRequestUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#getNoResultsText()
	 */
	@Override
	protected int getNoResultsText() {
		return R.string.noResultsFriendsRequests;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(new FriendRequestAdapter(getActivity(), friendRequestsUseCase.getFriendRequests()) {
					
					@Override
					public void onAccept(FriendRequest friendRequest) {
						acceptFriendRequestUseCase.setFriendRequest(friendRequest);
						acceptFriendRequestUseCase.setAsAccepted();
						executeUseCase(acceptFriendRequestUseCase);
					}
					
					@Override
					public void onReject(FriendRequest friendRequest) {
						acceptFriendRequestUseCase.setFriendRequest(friendRequest);
						acceptFriendRequestUseCase.setAsRejected();
						executeUseCase(acceptFriendRequestUseCase);
					}
				});
				dismissLoading();
			}
		});
	}
}