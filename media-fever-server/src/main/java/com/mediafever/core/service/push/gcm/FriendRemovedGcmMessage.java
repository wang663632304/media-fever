package com.mediafever.core.service.push.gcm;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRemovedGcmMessage extends AuthenticatedGcmMessage {
	
	private static final String MESSAGE_KEY = "friendRemoved";
	
	private static final String REMOVED_FRIEND_ID_KEY = "removedFriendId";
	
	public FriendRemovedGcmMessage(Long removedFriendId) {
		addParameter(REMOVED_FRIEND_ID_KEY, removedFriendId);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.DefaultGcmMessage#getMessageKey()
	 */
	@Override
	public String getMessageKey() {
		return MESSAGE_KEY;
	}
}
