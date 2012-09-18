package com.mediafever.usecase;

import java.util.List;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.google.inject.Inject;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Episode;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MarkAsWachedUseCase extends AbstractApiUseCase<APIService> {
	
	private Long userId;
	private List<UserWatchable<Episode>> episodesUserWatchables;
	private Boolean watched;
	
	@Inject
	public MarkAsWachedUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		List<Long> watchablesIds = Lists.newArrayList();
		for (UserWatchable<Episode> userWatchable : episodesUserWatchables) {
			watchablesIds.add(userWatchable.getWatchable().getId());
		}
		getApiService().markAsWatched(userId, watchablesIds, watched);
		for (UserWatchable<Episode> userWatchable : episodesUserWatchables) {
			userWatchable.modify(watched, userWatchable.isInWishList());
		}
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setWatched(Boolean watched) {
		this.watched = watched;
	}
	
	public void setEpisodesUserWatchables(List<UserWatchable<Episode>> episodesUserWatchables) {
		this.episodesUserWatchables = episodesUserWatchables;
	}
	
}