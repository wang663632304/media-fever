package com.mediafever.domain;

import java.util.List;
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
	private List<UserImpl> watchedBy;
	private List<UserImpl> onTheWishListOf;
	
	public UserWatchable(Long id, Boolean watched, Boolean isInWishList, T watchable, List<UserImpl> watchedBy,
			List<UserImpl> onTheWishListOf) {
		super(id);
		this.watched = watched;
		this.isInWishList = isInWishList;
		this.watchable = watchable;
		this.watchedBy = watchedBy;
		this.onTheWishListOf = onTheWishListOf;
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
	
	public List<UserImpl> getWatchedBy() {
		return watchedBy;
	}
	
	public List<UserImpl> getOnTheWishListOf() {
		return onTheWishListOf;
	}
	
}
