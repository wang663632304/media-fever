package com.mediafever.domain.watchable;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public class Movie extends Watchable {
	
	private String tagline;
	private String trailerURL;
	
	public Movie(Long id, String name, String tagline, String imageUrl, String overview, String trailerURL,
			List<String> actors, List<String> genres, Date releaseDate) {
		super(id, name, imageUrl, overview, actors, genres, releaseDate);
		this.tagline = tagline;
		this.trailerURL = trailerURL;
	}
	
	public String getTrailerURL() {
		return trailerURL;
	}
	
	public Boolean hasYoutubeTrailer() {
		return trailerURL != null ? trailerURL.startsWith("http://www.youtube.com") : false;
	}
	
	public String getTagline() {
		return tagline;
	}
	
}
