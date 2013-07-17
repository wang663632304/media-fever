package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.AdSize;
import com.jdroid.android.adapter.BaseArrayAdapter;
import com.jdroid.android.fragment.AbstractSearchFragment;
import com.jdroid.android.usecase.SearchUseCase;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Lists;
import com.mediafever.R;
import com.mediafever.android.ui.UserAdapter;
import com.mediafever.domain.UserImpl;
import com.mediafever.usecase.friends.SearchUsersUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SearchUsersFragment extends AbstractSearchFragment<UserImpl> {
	
	private SearchUsersUseCase searchUsersUseCase;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractSearchFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setTitle(R.string.searchUsers);
		setThreshold(3);
		
		searchUsersUseCase = getInstance(SearchUsersUseCase.class);
		searchUsersUseCase.setUser(getUser());
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
		return R.string.searchByNameOrEmail;
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
		CreateFriendRequestDialogFragment.show(getActivity(), user.getId(), user.getFullname());
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? null : super.getAdSize();
	}
}
