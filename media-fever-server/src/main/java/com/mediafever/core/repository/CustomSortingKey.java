package com.mediafever.core.repository;

import com.jdroid.javaweb.search.SortingKey;

/**
 * 
 * @author Maxi Rosson
 */
public enum CustomSortingKey implements SortingKey {
	
	NAME("name"),
	RELEASE_YEAR("releaseYear");
	
	private String property;
	
	private CustomSortingKey(String property) {
		this.property = property;
	}
	
	@Override
	public String getProperty() {
		return property;
	}
	
}
