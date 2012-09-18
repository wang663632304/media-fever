package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.google.ads.AdSize;
import com.mediafever.R;
import com.mediafever.domain.FriendRequest;
import com.mediafever.usecase.AcceptFriendRequestUseCase;
import com.mediafever.usecase.FriendRequestsUseCase;

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
		setRetainInstance(true);
		
		getSupportActionBar().setTitle(R.string.friendsRequests);
		
		if (friendRequestsUseCase == null) {
			friendRequestsUseCase = getInstance(FriendRequestsUseCase.class);
			friendRequestsUseCase.setUserId(getUser().getId());
		}
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
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(friendRequestsUseCase, this, true);
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
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? AdSize.IAB_BANNER : AdSize.SMART_BANNER;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#getNoResultsText()
	 */
	@Override
	protected int getNoResultsText() {
		return R.string.noResultsFriendsRequests;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getListView().setItemsCanFocus(true);
		
		if (acceptFriendRequestUseCase == null) {
			acceptFriendRequestUseCase = getInstance(AcceptFriendRequestUseCase.class);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				setListAdapter(new FriendRequestAdapter(FriendsRequestsFragment.this.getActivity(),
						friendRequestsUseCase.getFriendRequests()) {
					
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