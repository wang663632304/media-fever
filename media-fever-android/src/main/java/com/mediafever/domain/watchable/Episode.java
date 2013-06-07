package com.mediafever.domain.watchable;

import java.util.Date;

/**
 * 
 * @author Maxi Rosson
 */
public class Episode extends Watchable {
	
	private Integer episodeNumber;
	
	public Episode(Long id, String name, String imageURL, String overview, Date releaseDate, Integer episodeNumber) {
		super(id, name, imageURL, overview, null, null, releaseDate);
		this.episodeNumber = episodeNumber;
	}
	
	public Integer getEpisodeNumber() {
		return episodeNumber;
	}
	
}
