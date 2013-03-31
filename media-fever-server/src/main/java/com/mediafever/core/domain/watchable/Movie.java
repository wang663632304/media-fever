package com.mediafever.core.domain.watchable;

import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class Movie extends Watchable {
	
	private String tagline;
	private String trailerURL;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Movie() {
		// Do nothing, is required by hibernate
	}
	
	public Movie(Long externalId, String name, String imageURL, String overview, List<Person> actors,
			List<Genre> genres, Float rating, Integer ratingCount, Integer releaseYear, Long lastupdated,
			String tagline, String trailerURL) {
		super(externalId, name, imageURL, overview, actors, genres, rating, ratingCount, releaseYear, lastupdated);
		this.tagline = tagline;
		this.trailerURL = trailerURL;
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.Watchable#updateFrom(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void updateFrom(Watchable movie) {
		super.updateFrom(movie);
		Movie otherMovie = Movie.class.cast(movie);
		tagline = otherMovie.tagline;
		trailerURL = otherMovie.trailerURL;
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.Watchable#getType()
	 */
	@Override
	public WatchableType getType() {
		return WatchableType.MOVIE;
	}
	
	public String getTrailerURL() {
		return trailerURL;
	}
	
	public String getTagline() {
		return tagline;
	}
	
}
