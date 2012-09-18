package com.mediafever.domain.watchable;

import java.util.List;
import com.jdroid.android.domain.Entity;
import com.mediafever.domain.UserWatchable;

/**
 * 
 * @author Maxi Rosson
 */
public class Season extends Entity {
	
	private Integer seasonNumber;
	private List<UserWatchable<Episode>> episodesUserWatchables;
	
	public Season(Long id, Integer seasonNumber, List<UserWatchable<Episode>> episodesUserWatchables) {
		super(id);
		this.seasonNumber = seasonNumber;
		this.episodesUserWatchables = episodesUserWatchables;
	}
	
	public Integer getSeasonNumber() {
		return seasonNumber;
	}
	
	public List<UserWatchable<Episode>> getEpisodesUserWatchables() {
		return episodesUserWatchables;
	}
}
