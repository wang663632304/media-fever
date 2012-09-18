package com.mediafever.domain.watchingsession;

import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.User;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionUser extends Entity {
	
	private User user;
	private Boolean accepted;
	private Integer pendingThumbsUp;
	private Integer pendingThumbsDown;
	
	public WatchingSessionUser(User user, Boolean accepted, Integer pendingThumbsUp, Integer pendingThumbsDown) {
		this.user = user;
		this.accepted = accepted;
		this.pendingThumbsUp = pendingThumbsUp;
		this.pendingThumbsDown = pendingThumbsDown;
	}
	
	public Integer getPendingThumbsUp() {
		return pendingThumbsUp;
	}
	
	public Integer getPendingThumbsDown() {
		return pendingThumbsDown;
	}
	
	public Boolean isAccepted() {
		return accepted;
	}
	
	public User getUser() {
		return user;
	}
	
}
