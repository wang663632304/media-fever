package com.mediafever.android.ui.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.AndroidUseCaseListener;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.fragment.AbstractSearchFragment;
import com.jdroid.android.usecase.SearchUseCase;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Lists;
import com.mediafever.R;
import com.mediafever.android.ui.UserAdapter;
import com.mediafever.domain.UserImpl;
import com.mediafever.usecase.friends.CreateFriendRequestUseCase;
import com.mediafever.usecase.friends.SearchUsersUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SearchUsersFragment extends AbstractSearchFragment<UserImpl> {
	
	private SearchUsersUseCase searchUsersUseCase;
	private CreateFriendRequestUseCase createFriendRequestUseCase;
	private AndroidUseCaseListener createFriendRequestUseCaseListener;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		getSupportActionBar().setTitle(R.string.searchUsers);
		setThreshold(3);
		
		searchUsersUseCase = getInstance(SearchUsersUseCase.class);
		searchUsersUseCase.setUser(getUser());
		
		createFriendRequestUseCase = getInstance(CreateFriendRequestUseCase.class);
		createFriendRequestUseCaseListener = new AndroidUseCaseListener() {
			
			@Override
			public void onFinishUseCase() {
				executeOnUIThread(new Runnable() {
					
					@Override
					public void run() {
						dismissLoading();
						if (createFriendRequestUseCase.wasAddAsFriend()) {
							ToastUtils.showInfoToast(getString(R.string.addedAsFriend,
								createFriendRequestUseCase.getUser().getFullname()));
						} else {
							ToastUtils.showInfoToast(getString(R.string.invitedToBeYourFriend,
								createFriendRequestUseCase.getUser().getFullname()));
						}
					}
				});
			}
			
			@Override
			protected ActivityIf getActivityIf() {
				return (ActivityIf)getActivity();
			}
		};
	}
	
	/**
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.search_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#getSearchEditTextHintResId()
	 */
	@Override
	protected int getSearchEditTextHintResId() {
		return R.string.searchBy;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(createFriendRequestUseCase, createFriendRequestUseCaseListener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(createFriendRequestUseCase, createFriendRequestUseCaseListener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#getSearchUseCase()
	 */
	@Override
	protected SearchUseCase<UserImpl> getSearchUseCase() {
		return searchUsersUseCase;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#createBaseArrayAdapter()
	 */
	@Override
	protected BaseArrayAdapter<UserImpl> createBaseArrayAdapter() {
		return new UserAdapter(getActivity(), Lists.<UserImpl>newArrayList(), R.layout.user_small_item);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#isSearchValueRequired()
	 */
	@Override
	public boolean isSearchValueRequired() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(final UserImpl user) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.friendRequest);
		builder.setMessage(getString(R.string.addUser, user.getFullname()));
		builder.setPositiveButton(R.string.yes, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				createFriendRequestUseCase.setUser(user);
				executeUseCase(createFriendRequestUseCase);
			}
		});
		builder.setNegativeButton(R.string.no, null);
		builder.show();
	}
}
