package com.mediafever.core.domain.watchable;

import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Watchable extends Entity {
	
	private Long externalId;
	private String name;
	private String imageURL;
	private String overview;
	
	@OneToMany(targetEntity = Person.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinTable(name = "Watchable_Actor", joinColumns = @JoinColumn(name = "watchableId"),
			inverseJoinColumns = @JoinColumn(name = "actorId"))
	private List<Person> actors;
	
	@ElementCollection
	@JoinTable(name = "Watchable_Genre", joinColumns = @JoinColumn(name = "watchableId"))
	@Column(name = "genre")
	@Enumerated(value = EnumType.STRING)
	private List<Genre> genres;
	
	private Float rating;
	private Integer ratingCount;
	
	private Integer releaseYear;
	private Long lastupdated;
	
	protected Watchable() {
		// Do nothing, is required by hibernate
	}
	
	public Watchable(Long externalId, String name, String imageURL, String overview, List<Person> actors,
			List<Genre> genres, Float rating, Integer ratingCount, Integer releaseYear, Long lastupdated) {
		this.externalId = externalId;
		this.name = name;
		this.imageURL = imageURL;
		this.overview = overview;
		this.actors = actors;
		this.genres = genres;
		this.rating = rating != null ? rating : 0f;
		this.ratingCount = ratingCount != null ? ratingCount : 0;
		this.releaseYear = releaseYear;
		this.lastupdated = lastupdated;
	}
	
	/**
	 * Updates this {@link Watchable} with the data contained in the {@link Watchable} passed as parameter.
	 * 
	 * @param watchable The {@link Watchable} with the new data.
	 */
	public void updateFrom(Watchable watchable) {
		imageURL = watchable.imageURL;
		lastupdated = watchable.lastupdated;
		name = watchable.name;
		overview = watchable.overview;
		releaseYear = watchable.releaseYear;
		rating = watchable.rating;
		ratingCount = watchable.ratingCount;
		
		// Update the actors
		if (watchable.actors != null) {
			if (actors == null) {
				actors = Lists.newArrayList();
			}
			Map<String, Person> actorsMap = Maps.newHashMap();
			for (Person actor : watchable.actors) {
				actorsMap.put(actor.getFullname(), actor);
			}
			List<Person> actorsToRemove = Lists.newArrayList();
			for (Person actor : actors) {
				Person otherActor = actorsMap.get(actor.getFullname());
				if (otherActor != null) {
					actorsMap.remove(otherActor.getFullname());
				} else {
					actorsToRemove.add(actor);
				}
			}
			actors.removeAll(actorsToRemove);
			actors.addAll(actorsMap.values());
		}
		
		// Update the Genres
		if (watchable.genres != null) {
			if (genres == null) {
				genres = Lists.newArrayList();
			}
			List<Genre> newGenres = Lists.newArrayList(watchable.genres);
			List<Genre> genresToRemove = Lists.newArrayList();
			for (Genre genre : genres) {
				if (newGenres.contains(genre)) {
					newGenres.remove(genre);
				} else {
					genresToRemove.add(genre);
				}
			}
			genres.removeAll(genresToRemove);
			genres.addAll(newGenres);
		}
	}
	
	public void addRating(Float newRating) {
		float totalRating = (rating * ratingCount) + newRating;
		ratingCount++;
		rating = totalRating / ratingCount;
	}
	
	public abstract WatchableType getType();
	
	public Long getExternalId() {
		return externalId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImageURL() {
		return imageURL;
	}
	
	public String getOverview() {
		return overview;
	}
	
	public List<Person> getActors() {
		return actors;
	}
	
	public List<Genre> getGenres() {
		return genres;
	}
	
	public Long getLastupdated() {
		return lastupdated;
	}
	
	public Integer getReleaseYear() {
		return releaseYear;
	}
	
	/**
	 * @return The {@link Watchable} rating between 1 and 5. 0 means no votes
	 */
	public Float getRating() {
		return rating;
	}
	
	public Integer getRatingCount() {
		return ratingCount;
	}
	
}
