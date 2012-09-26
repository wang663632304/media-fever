package com.mediafever.core.domain.session;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import com.jdroid.javaweb.domain.Entity;
import com.mediafever.core.domain.User;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class MediaSessionUser extends Entity {
	
	@OneToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	private Boolean accepted;
	private Integer pendingThumbsUp;
	private Integer pendingThumbsDown;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private MediaSessionUser() {
		// Do nothing, is required by hibernate
	}
	
	public MediaSessionUser(User user) {
		this.user = user;
		pendingThumbsUp = 10;
		pendingThumbsDown = 10;
	}
	
	public User getUser() {
		return user;
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
	
	public void accept() {
		accepted = true;
	}
	
	public void reject() {
		accepted = false;
	}
}
