package com.mediafever.core.domain;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.jdroid.javaweb.domain.Entity;
import com.mediafever.core.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class UserWatchable extends Entity {
	
	@OneToOne
	@JoinColumn(name = "watchableId", nullable = false)
	private Watchable watchable;
	
	@OneToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	private Boolean watched;
	private Boolean isInWishList;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private UserWatchable() {
		// Do nothing, is required by hibernate
	}
	
	public UserWatchable(User user, Watchable watchable, Boolean watched, Boolean isInWishList) {
		this.user = user;
		this.watchable = watchable;
		this.watched = watched;
		this.isInWishList = isInWishList;
	}
	
	public UserWatchable(User user, Watchable watchable) {
		this(user, watchable, false, false);
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
	
	public Watchable getWatchable() {
		return watchable;
	}
	
	public User getUser() {
		return user;
	}
}
