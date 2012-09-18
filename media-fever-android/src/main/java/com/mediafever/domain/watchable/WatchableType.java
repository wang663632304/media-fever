package com.mediafever.domain.watchable;

import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.R;
import com.mediafever.parser.EpisodeParser;
import com.mediafever.parser.MovieParser;
import com.mediafever.parser.SeriesParser;

/**
 * 
 * @author Maxi Rosson
 */
public enum WatchableType {
	
	MOVIE("Movie", R.string.movie, R.drawable.movie_icon, Movie.class, new MovieParser()),
	SERIES("Series", R.string.series, R.drawable.series_icon, Series.class, new SeriesParser()),
	EPISODE("Episode", null, null, Episode.class, new EpisodeParser());
	
	private String name;
	private Integer resourceId;
	private Integer iconId;
	private Class<? extends Watchable> watchableClass;
	private JsonParser<JsonObjectWrapper> parser;
	
	private WatchableType(String name, Integer resourceId, Integer iconId, Class<? extends Watchable> watchableClass,
			JsonParser<JsonObjectWrapper> parser) {
		this.name = name;
		this.resourceId = resourceId;
		this.iconId = iconId;
		this.watchableClass = watchableClass;
		this.parser = parser;
	}
	
	public static WatchableType find(Watchable watchable) {
		WatchableType watchableType = null;
		for (WatchableType each : values()) {
			if (each.watchableClass.equals(watchable.getClass())) {
				watchableType = each;
				break;
			}
		}
		return watchableType;
	}
	
	public static WatchableType find(String name) {
		WatchableType watchableType = null;
		for (WatchableType each : values()) {
			if (each.name.equals(name)) {
				watchableType = each;
				break;
			}
		}
		return watchableType;
	}
	
	public JsonParser<JsonObjectWrapper> getParser() {
		return parser;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getResourceId() {
		return resourceId;
	}
	
	public Integer getIconId() {
		return iconId;
	}
	
	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
}
