package com.mediafever.core.domain.watchable;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.common.collect.Lists;

/**
 * 
 * @author Maxi Rosson
 */
public enum Genre {
	
	ACTION("Action"),
	ADVENTURE("Adventure"),
	ANIMATION("Animation"),
	CHILDREN("Children"),
	COMEDY("Comedy"),
	CRIME("Crime"),
	DISASTER("Disaster"),
	DOCUMENTARY("Documentary"),
	DRAMA("Drama"),
	EASTERN("Eastern"),
	FAMILY("Family"),
	FANTASY("Fantasy"),
	FILM_NOIR("Film Noir"),
	FOREIGN("Foreign"),
	HISTORY("History"),
	HOLIDAY("Holiday"),
	HOME_AND_GARDEN("Home and Garden"),
	HORROR("Horror"),
	INDIE("Indie"),
	MINI_SERIES("Mini-Series"),
	MUSIC("Music"),
	MUSICAL("Musical"),
	MYSTERY("Mystery"),
	NEO_NOIR("Neo-noir"),
	NEWA("News"),
	REALITY("Reality"),
	ROAD_MOVIE("Road Movie"),
	ROMANCE("Romance"),
	SCIENCE_FICTION("Science Fiction", "Science-Fiction"),
	SHORT("Short"),
	SOAP("Soap"),
	SPORT("Sport"),
	SPORTS_FILM("Sports Film"),
	SUSPENSE("Suspense"),
	TALK_SHOW("Talk Show"),
	THRILLER("Thriller"),
	WAR("War"),
	WESTERN("Western");
	
	private static final Log LOG = LogFactory.getLog(Genre.class);
	
	private List<String> names;
	
	private Genre(String... names) {
		this.names = Lists.newArrayList(names);
	}
	
	public static Genre findByName(String name) {
		Genre genre = null;
		for (Genre each : values()) {
			if (each.names.contains(name)) {
				genre = each;
				break;
			}
		}
		if (genre == null) {
			LOG.warn("The genre [" + name + "] is not supported.");
		}
		return genre;
	}
	
	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return names.get(0);
	}
}
