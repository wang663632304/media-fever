package com.mediafever.core.domain.watchable;

import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdroid.javaweb.domain.Entity;
import com.mediafever.core.domain.watchable.visitor.DummyWatchableVisitor;
import com.mediafever.core.domain.watchable.visitor.WatchableVisitor;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class Season extends Entity {
	
	private Long externalId;
	private Integer seasonNumber;
	
	@OneToMany(targetEntity = Episode.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinTable(name = "Season_Episode", joinColumns = @JoinColumn(name = "seasonId"), inverseJoinColumns = @JoinColumn(
			name = "episodeId"))
	private List<Episode> episodes;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private Season() {
		// Do nothing, is required by hibernate
	}
	
	public Season(Long externalId, Integer seasonNumber) {
		this.externalId = externalId;
		this.seasonNumber = seasonNumber;
		episodes = Lists.newArrayList();
	}
	
	public void addEpisode(Episode episode) {
		episodes.add(episode);
	}
	
	public Long getExternalId() {
		return externalId;
	}
	
	public Integer getSeasonNumber() {
		return seasonNumber;
	}
	
	public List<Episode> getEpisodes() {
		return episodes;
	}
	
	public List<Episode> getReleasedEpisodes() {
		return Lists.newArrayList(Iterables.filter(episodes, new Predicate<Episode>() {
			
			@Override
			public boolean apply(Episode episode) {
				return episode.isReleased();
			}
		}));
	}
	
	public void updateFrom(Season season) {
		updateFrom(season, new DummyWatchableVisitor());
	}
	
	public void updateFrom(Season season, WatchableVisitor watchableVisitor) {
		seasonNumber = season.seasonNumber;
		
		// Update the seasons
		if (season.episodes != null) {
			if (episodes == null) {
				episodes = Lists.newArrayList();
			}
			Map<Long, Episode> episodesMap = Maps.newHashMap();
			for (Episode episode : season.episodes) {
				episodesMap.put(episode.getExternalId(), episode);
			}
			List<Episode> episodesToRemove = Lists.newArrayList();
			for (Episode episode : episodes) {
				Episode otherEpisode = episodesMap.get(episode.getExternalId());
				if (otherEpisode != null) {
					episode.updateFrom(otherEpisode);
					watchableVisitor.visitUpdated(episode);
					episodesMap.remove(otherEpisode.getExternalId());
				} else {
					episodesToRemove.add(episode);
					watchableVisitor.visitDeleted(episode);
				}
			}
			episodes.removeAll(episodesToRemove);
			episodes.addAll(episodesMap.values());
			
			// visit new episodes
			for (Episode episode : episodesMap.values()) {
				watchableVisitor.visitNew(episode);
			}
		}
	}
}
