package com.mediafever.domain.watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class Episode extends Watchable {
	
	private Integer episodeNumber;
	
	public Episode(Long id, String name, String imageURL, String overview, Integer releaseYear, Integer episodeNumber) {
		super(id, name, imageURL, overview, null, null, releaseYear);
		this.episodeNumber = episodeNumber;
	}
	
	public Integer getEpisodeNumber() {
		return episodeNumber;
	}
	
}
