package com.mediafever.domain.session;

import java.util.List;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.User;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelection extends Entity {
	
	private Watchable watchable;
	private List<MediaSessionUser> thumbsUpUsers = Lists.newArrayList();
	private List<MediaSessionUser> thumbsDownUsers = Lists.newArrayList();
	private User owner;
	
	public MediaSelection() {
	}
	
	public MediaSelection(Watchable watchable) {
		this(null, watchable, SecurityContext.get().getUser(), null, null);
	}
	
	public MediaSelection(Long id, Watchable watchable, User owner, List<MediaSessionUser> thumbsUpUsers,
			List<MediaSessionUser> thumbsDownUsers) {
		super(id);
		this.watchable = watchable;
		this.owner = owner;
		this.thumbsUpUsers = Lists.safeArrayList(thumbsUpUsers);
		this.thumbsDownUsers = Lists.safeArrayList(thumbsDownUsers);
	}
	
	public void thumbsUp(MediaSessionUser mediaSessionUser) {
		thumbsDownUsers.remove(mediaSessionUser);
		if (!thumbsUpUsers.contains(mediaSessionUser)) {
			thumbsUpUsers.add(mediaSessionUser);
		}
	}
	
	public void thumbsDown(MediaSessionUser mediaSessionUser) {
		thumbsUpUsers.remove(mediaSessionUser);
		if (!thumbsDownUsers.contains(mediaSessionUser)) {
			thumbsDownUsers.add(mediaSessionUser);
		}
	}
	
	/**
	 * @return the watchable
	 */
	public Watchable getWatchable() {
		return watchable;
	}
	
	/**
	 * @return the thumbsUpUsers
	 */
	public List<MediaSessionUser> getThumbsUpUsers() {
		return thumbsUpUsers;
	}
	
	/**
	 * @return the thumbsDownUsers
	 */
	public List<MediaSessionUser> getThumbsDownUsers() {
		return thumbsDownUsers;
	}
	
	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}
}
