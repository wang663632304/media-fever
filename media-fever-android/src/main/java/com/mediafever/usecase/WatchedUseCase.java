package com.mediafever.usecase;

import java.util.List;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchedUseCase extends AbstractApiUseCase<APIService> {
	
	private List<Watchable> watchables;
	private List<WatchableType> watchableTypes = Lists.newArrayList();
	private Long userId;
	
	@Inject
	public WatchedUseCase(APIService apiService) {
		super(apiService);
		watchableTypes.add(WatchableType.MOVIE);
		watchableTypes.add(WatchableType.SERIES);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		watchables = getApiService().searchWatched(userId, watchableTypes).getResults();
	}
	
	/**
	 * @return the watchables
	 */
	public List<Watchable> getWatchables() {
		return watchables;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
