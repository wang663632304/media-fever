package com.mediafever.core.domain.watchable;

import java.util.Date;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class Episode extends Watchable {
	
	private Integer episodeNumber;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Episode() {
		// Do nothing, is required by hibernate
	}
	
	public Episode(Long externalId, String name, String imageURL, String overview, Float rating, Integer ratingCount,
			Date releaseDate, Long lastupdated, Integer episodeNumber) {
		super(externalId, name, imageURL, overview, null, null, rating, ratingCount, releaseDate, lastupdated);
		this.episodeNumber = episodeNumber;
	}
	
	public Integer getEpisodeNumber() {
		return episodeNumber;
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.Watchable#updateFrom(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void updateFrom(Watchable episode) {
		super.updateFrom(episode);
		Episode otherEpisode = Episode.class.cast(episode);
		episodeNumber = otherEpisode.episodeNumber;
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.Watchable#getType()
	 */
	@Override
	public WatchableType getType() {
		return WatchableType.EPISODE;
	}
}
