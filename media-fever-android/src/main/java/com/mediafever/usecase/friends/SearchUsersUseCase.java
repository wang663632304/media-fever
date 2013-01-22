package com.mediafever.usecase.friends;

import com.google.inject.Inject;
import com.jdroid.android.domain.User;
import com.jdroid.android.search.SearchResult;
import com.jdroid.android.usecase.SearchApiUseCase;
import com.mediafever.domain.UserImpl;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class SearchUsersUseCase extends SearchApiUseCase<UserImpl, APIService> {
	
	private User user;
	
	@Inject
	public SearchUsersUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.SearchUseCase#doSearch(java.lang.String)
	 */
	@Override
	protected SearchResult<UserImpl> doSearch(String searchValue) {
		return SearchResult.getInstance(getApiService().searchUsers(user.getId(), searchValue));
	}
	
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
}
