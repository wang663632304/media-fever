package com.mediafever.domain.watchable;

import java.util.List;
import com.jdroid.java.utils.CollectionUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class Series extends Watchable {
	
	private List<Season> seasons;
	
	public Series(Long id, String name, String imageUrl, String overview, List<String> actors, List<String> genres,
			Integer releaseYear, List<Season> seasons) {
		super(id, name, imageUrl, overview, actors, genres, releaseYear);
		this.seasons = seasons;
	}
	
	public List<Season> getSeasons() {
		return seasons;
	}
	
	public Boolean hasSeasons() {
		return CollectionUtils.isNotEmpty(seasons);
	}
}