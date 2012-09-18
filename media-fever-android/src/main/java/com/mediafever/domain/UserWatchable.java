package com.mediafever.domain;

import com.jdroid.android.domain.Entity;
import com.mediafever.domain.watchable.Watchable;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public class UserWatchable<T extends Watchable> extends Entity {
	
	private Boolean watched;
	private Boolean isInWishList;
	private T watchable;
	
	public UserWatchable(Long id, Boolean watched, Boolean isInWishList, T watchable) {
		super(id);
		this.watched = watched;
		this.isInWishList = isInWishList;
		this.watchable = watchable;
	}
	
	public void modify(Boolean watched, Boolean isInWishList) {
		this.watched = watched;
		this.isInWishList = isInWishList;
	}
	
	public Boolean isWatched() {
		return watched;
	}
	
	public Boolean isInWishList() {
		return isInWishList;
	}
	
	public T getWatchable() {
		return watchable;
	}
	
}
