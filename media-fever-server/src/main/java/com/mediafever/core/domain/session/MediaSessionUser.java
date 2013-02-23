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
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private MediaSessionUser() {
		// Do nothing, is required by hibernate
	}
	
	public MediaSessionUser(User user, Boolean accepted) {
		this.user = user;
		this.accepted = accepted;
	}
	
	public MediaSessionUser(User user) {
		this(user, null);
	}
	
	public User getUser() {
		return user;
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
