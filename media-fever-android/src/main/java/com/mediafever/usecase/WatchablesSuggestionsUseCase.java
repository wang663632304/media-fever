package com.mediafever.usecase;

import java.util.List;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchablesSuggestionsUseCase extends AbstractApiUseCase<APIService> {
	
	private String query;
	private List<Watchable> watchables;
	private List<WatchableType> watchableTypes = Lists.newArrayList();
	
	@Inject
	public WatchablesSuggestionsUseCase(APIService apiService) {
		super(apiService);
		watchableTypes.add(WatchableType.MOVIE);
		watchableTypes.add(WatchableType.SERIES);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (StringUtils.isNotEmpty(query)) {
			watchables = getApiService().searchSuggestedWatchables(query, watchableTypes);
		} else {
			watchables = Lists.newArrayList();
		}
	}
	
	/**
	 * @return the watchables
	 */
	public List<Watchable> getWatchables() {
		return watchables;
	}
	
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	
}
