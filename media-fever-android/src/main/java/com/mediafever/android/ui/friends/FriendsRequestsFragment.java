package com.mediafever.android.ui.friends;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.AdSize;
import com.jdroid.android.AndroidUseCaseListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.gcm.GcmMessage;
import com.jdroid.android.gcm.GcmMessageBroadcastReceiver;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.android.gcm.MediaFeverGcmMessage;
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
	private AndroidUseCaseListener acceptFriendRequestUseCaseListener;
	
	private BroadcastReceiver refreshBroadcastReceiver;
	
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
		acceptFriendRequestUseCaseListener = new AndroidUseCaseListener() {
			
			@Override
			public void onFinishUseCase() {
				friendRequestsUseCase.removeFriendRequest(acceptFriendRequestUseCase.getFriendRequest());
				FriendsRequestsFragment.this.onFinishUseCase();
			}
			
			@Override
			public Boolean goBackOnError(RuntimeException runtimeException) {
				return false;
			}
			
			@Override
			protected ActivityIf getActivityIf() {
				return (ActivityIf)getActivity();
			}
		};
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
		onResumeUseCase(acceptFriendRequestUseCase, acceptFriendRequestUseCaseListener);
		
		refreshBroadcastReceiver = new GcmMessageBroadcastReceiver() {
			
			@Override
			protected void onGcmMessage(GcmMessage gcmMessage, Intent intent) {
				executeUseCase(friendRequestsUseCase);
			}
		};
		
		GcmMessageBroadcastReceiver.startListeningGcmBroadcasts(refreshBroadcastReceiver,
			MediaFeverGcmMessage.FRIEND_REQUEST);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(friendRequestsUseCase, this);
		onPauseUseCase(acceptFriendRequestUseCase, acceptFriendRequestUseCaseListener);
		
		GcmMessageBroadcastReceiver.stopListeningGcmBroadcasts(refreshBroadcastReceiver);
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
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? null : super.getAdSize();
	}
}