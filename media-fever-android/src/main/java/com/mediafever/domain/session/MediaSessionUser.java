package com.mediafever.domain.session;

import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.User;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionUser extends Entity {
	
	private User user;
	private Boolean accepted;
	private Integer pendingThumbsUp;
	private Integer pendingThumbsDown;
	
	public MediaSessionUser(User user) {
		this.user = user;
		pendingThumbsUp = 10;
		pendingThumbsDown = 10;
	}
	
	public MediaSessionUser(User user, Boolean accepted, Integer pendingThumbsUp, Integer pendingThumbsDown) {
		this.user = user;
		this.accepted = accepted;
		this.pendingThumbsUp = pendingThumbsUp != null ? pendingThumbsUp : 0;
		this.pendingThumbsDown = pendingThumbsDown != null ? pendingThumbsDown : 0;
	}
	
	public Integer getPendingThumbsUp() {
		return pendingThumbsUp;
	}
	
	public Boolean hasPendingThumbsUp() {
		return pendingThumbsUp > 0;
	}
	
	public void decrementPendingThumbsUp() {
		pendingThumbsUp--;
	}
	
	public Integer getPendingThumbsDown() {
		return pendingThumbsDown;
	}
	
	public void decrementPendingThumbsDown() {
		pendingThumbsDown--;
	}
	
	public Boolean hasPendingThumbsDown() {
		return pendingThumbsDown > 0;
	}
	
	public Boolean isAccepted() {
		return accepted;
	}
	
	public User getUser() {
		return user;
	}
	
}
