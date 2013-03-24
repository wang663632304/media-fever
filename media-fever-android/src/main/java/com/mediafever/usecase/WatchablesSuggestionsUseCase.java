package com.mediafever.usecase;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.search.SearchResult;
import com.jdroid.android.usecase.SearchApiUseCase;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchablesSuggestionsUseCase extends SearchApiUseCase<Watchable, APIService> {
	
	private List<WatchableType> watchableTypes = Lists.newArrayList();
	
	@Inject
	public WatchablesSuggestionsUseCase(APIService apiService) {
		super(apiService);
		watchableTypes.add(WatchableType.MOVIE);
		watchableTypes.add(WatchableType.SERIES);
	}
	
	/**
	 * @see com.jdroid.android.usecase.SearchUseCase#doSearch(java.lang.String)
	 */
	@Override
	protected SearchResult<Watchable> doSearch(String searchValue) {
		List<Watchable> watchables;
		if (StringUtils.isNotEmpty(searchValue)) {
			watchables = getApiService().searchSuggestedWatchables(searchValue, watchableTypes);
		} else {
			watchables = Lists.newArrayList();
		}
		return SearchResult.getInstance(watchables);
	}
}
