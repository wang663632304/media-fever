package com.mediafever.core.domain;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import com.jdroid.javaweb.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class FriendRequest extends Entity {
	
	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;
	
	@OneToOne
	@JoinColumn(name = "senderId", nullable = false)
	private User sender;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private FriendRequest() {
		// Do nothing, is required by hibernate
	}
	
	public FriendRequest(User user, User sender) {
		this.user = user;
		this.sender = sender;
	}
	
	public void accept() {
		user.acceptFriendRequest(this);
	}
	
	public void reject() {
		user.rejectFriendRequest(this);
	}
	
	public User getUser() {
		return user;
	}
	
	public User getSender() {
		return sender;
	}
	
}
