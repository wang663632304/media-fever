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
	
	public MediaSessionUser(User user) {
		this.user = user;
	}
	
	public MediaSessionUser(User user, Boolean accepted) {
		this.user = user;
		this.accepted = accepted;
	}
	
	public Boolean isAccepted() {
		return accepted;
	}
	
	public User getUser() {
		return user;
	}
	
}
