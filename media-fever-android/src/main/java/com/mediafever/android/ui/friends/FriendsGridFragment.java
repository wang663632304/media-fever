package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.AdSize;
import com.jdroid.android.AndroidUseCaseListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.utils.AnimationUtils;
import com.jdroid.android.utils.ToastUtils;
import com.mediafever.R;
import com.mediafever.android.ui.UserAdapter;
import com.mediafever.domain.UserImpl;
import com.mediafever.usecase.friends.FriendsUseCase;
import com.mediafever.usecase.friends.RemoveFriendUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendsGridFragment extends AbstractGridFragment<UserImpl> {
	
	private RemoveFriendUseCase removeFriendUseCase;
	private FriendsUseCase friendsUseCase;
	private AndroidUseCaseListener removeFriendUseCaseListener;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		getSupportActionBar().setTitle(R.string.friends);
		
		if (friendsUseCase == null) {
			friendsUseCase = getInstance(FriendsUseCase.class);
			friendsUseCase.setUserId(getUser().getId());
		}
		
		if (removeFriendUseCase == null) {
			removeFriendUseCase = getInstance(RemoveFriendUseCase.class);
			removeFriendUseCaseListener = new AndroidUseCaseListener() {
				
				@Override
				public void onFinishUseCase() {
					executeOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							ToastUtils.showInfoToast(R.string.friendRemoved);
							executeUseCase(friendsUseCase);
							dismissLoading();
						}
					});
				}
				
				@Override
				protected ActivityIf getActivityIf() {
					return (ActivityIf)getActivity();
				}
			};
			removeFriendUseCase.setUserId(getUser().getId());
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(friendsUseCase, this, UseCaseTrigger.ONCE);
		onResumeUseCase(removeFriendUseCase, removeFriendUseCaseListener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(friendsUseCase, this);
		onPauseUseCase(removeFriendUseCase, removeFriendUseCaseListener);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.grid_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getGridView());
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View,
	 *      android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.friends_menu, menu);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		UserImpl friend = getMenuItem(item);
		switch (item.getItemId()) {
			case R.id.removeItem:
				removeFriendUseCase.setFriendId(friend.getId());
				executeUseCase(removeFriendUseCase);
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#getNoResultsText()
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
				setListAdapter(new UserAdapter(FriendsGridFragment.this.getActivity(), friendsUseCase.getFriends()));
				AnimationUtils.makeViewGroupAnimation(getGridView());
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return null;
	}
}
