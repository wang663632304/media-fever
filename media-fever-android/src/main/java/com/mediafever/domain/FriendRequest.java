package com.mediafever.domain;

import com.jdroid.android.domain.Entity;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequest extends Entity {
	
	private UserImpl sender;
	
	public FriendRequest(Long id, UserImpl sender) {
		super(id);
		this.sender = sender;
	}
	
	public UserImpl getSender() {
		return sender;
	}
}
