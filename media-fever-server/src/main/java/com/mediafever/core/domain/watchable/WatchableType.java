package com.mediafever.core.domain.watchable;

import java.util.List;
import com.google.common.collect.Lists;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.utils.StringUtils;

/**
 * 
 * @author Maxi Rosson
 */
public enum WatchableType {
	
	MOVIE("Movie", Movie.class),
	SERIES("Series", Series.class),
	EPISODE("Episode", Episode.class);
	
	private String name;
	private Class<? extends Watchable> watchableClass;
	
	private WatchableType(String name, Class<? extends Watchable> watchableClass) {
		this.name = name;
		this.watchableClass = watchableClass;
	}
	
	public static List<WatchableType> findByNames(String namesCSV) {
		List<WatchableType> watchableTypes = Lists.newArrayList();
		for (String name : StringUtils.splitToCollection(namesCSV)) {
			watchableTypes.add(WatchableType.findByName(name));
		}
		return watchableTypes;
	}
	
	public static WatchableType findByName(String name) {
		WatchableType watchableType = null;
		for (WatchableType each : values()) {
			if (each.name.equals(name)) {
				watchableType = each;
				break;
			}
		}
		if (watchableType == null) {
			throw new UnexpectedException("The WatchableType [" + name + "] is not supported.");
		}
		return watchableType;
	}
	
	public String getWatchableSimpleClassName() {
		return watchableClass.getSimpleName();
	}
	
	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
}
