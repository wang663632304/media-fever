package com.mediafever.usecase;

import java.util.List;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class LatestWatchablesUseCase extends AbstractApiUseCase<APIService> {
	
	private List<Watchable> watchables;
	
	@Inject
	public LatestWatchablesUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		watchables = getApiService().getLatestWatchables().getResults();
	}
	
	/**
	 * @return the watchables
	 */
	public List<Watchable> getWatchables() {
		return watchables;
	}
}
