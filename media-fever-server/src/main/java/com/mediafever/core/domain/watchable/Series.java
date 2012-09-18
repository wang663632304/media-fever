package com.mediafever.core.domain.watchable;

import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class Series extends Watchable {
	
	@OneToMany(targetEntity = Season.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinTable(name = "Series_Season", joinColumns = @JoinColumn(name = "seriesId"), inverseJoinColumns = @JoinColumn(
			name = "seasonId"))
	private List<Season> seasons;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Series() {
		// Do nothing, is required by hibernate
	}
	
	public Series(Long externalId, String name, String imageURL, String overview, List<Person> actors,
			List<Genre> genres, Float rating, Integer ratingCount, Integer releaseYear, Long lastupdated) {
		super(externalId, name, imageURL, overview, actors, genres, rating, ratingCount, releaseYear, lastupdated);
		seasons = Lists.newArrayList();
	}
	
	public void addSeason(Season season) {
		seasons.add(season);
	}
	
	public List<Season> getSeasons() {
		return seasons;
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.Watchable#updateFrom(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void updateFrom(Watchable series) {
		super.updateFrom(series);
		Series otherSeries = Series.class.cast(series);
		
		// Update the seasons
		if (otherSeries.seasons != null) {
			if (seasons == null) {
				seasons = Lists.newArrayList();
			}
			Map<Long, Season> seasonsMap = Maps.newHashMap();
			for (Season season : otherSeries.seasons) {
				seasonsMap.put(season.getExternalId(), season);
			}
			List<Season> seasonsToRemove = Lists.newArrayList();
			for (Season season : seasons) {
				Season otherSeason = seasonsMap.get(season.getExternalId());
				if (otherSeason != null) {
					season.updateFrom(otherSeason);
					seasonsMap.remove(otherSeason.getExternalId());
				} else {
					seasonsToRemove.add(season);
				}
			}
			seasons.removeAll(seasonsToRemove);
			seasons.addAll(seasonsMap.values());
		}
	}
}
