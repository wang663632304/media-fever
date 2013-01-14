package com.mediafever.domain;

import com.jdroid.android.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequest extends Entity {
	
	private UserImpl user;
	private UserImpl sender;
	
	public FriendRequest(Long id, UserImpl user, UserImpl sender) {
		super(id);
		this.user = user;
		this.sender = sender;
	}
	
	public UserImpl getSender() {
		return sender;
	}
	
	public UserImpl getUser() {
		return user;
	}
}
