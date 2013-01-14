package com.mediafever.usecase.friends;

import com.google.inject.Inject;
import com.jdroid.android.search.SearchResult;
import com.jdroid.android.usecase.SearchUseCase;
import com.mediafever.domain.UserImpl;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class SearchUsersUseCase extends SearchUseCase<UserImpl> {
	
	private APIService apiService;
	
	@Inject
	public SearchUsersUseCase(APIService apiService) {
		this.apiService = apiService;
	}
	
	/**
	 * @see com.jdroid.android.usecase.SearchUseCase#doSearch(java.lang.String)
	 */
	@Override
	protected SearchResult<UserImpl> doSearch(String searchValue) {
		return SearchResult.getInstance(apiService.searchUsers(searchValue));
	}
	
}
