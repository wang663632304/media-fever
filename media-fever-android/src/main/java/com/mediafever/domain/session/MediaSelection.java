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
public class MediaSelection extends Entity implements Comparable<MediaSelection> {
	
	private Watchable watchable;
	private List<User> thumbsUpUsers = Lists.newArrayList();
	private List<User> thumbsDownUsers = Lists.newArrayList();
	private User owner;
	
	public MediaSelection() {
	}
	
	public MediaSelection(Watchable watchable) {
		this(null, watchable, SecurityContext.get().getUser(), null, null);
	}
	
	public MediaSelection(Long id, Watchable watchable, User owner, List<User> thumbsUpUsers, List<User> thumbsDownUsers) {
		super(id);
		this.watchable = watchable;
		this.owner = owner;
		this.thumbsUpUsers = Lists.safeArrayList(thumbsUpUsers);
		this.thumbsDownUsers = Lists.safeArrayList(thumbsDownUsers);
	}
	
	public Boolean isRemovable() {
		return owner.equals(SecurityContext.get().getUser());
	}
	
	public void thumbsUp() {
		User user = SecurityContext.get().getUser();
		thumbsDownUsers.remove(user);
		if (!thumbsUpUsers.contains(user)) {
			thumbsUpUsers.add(user);
		}
	}
	
	public void thumbsDown() {
		User user = SecurityContext.get().getUser();
		thumbsUpUsers.remove(user);
		if (!thumbsDownUsers.contains(user)) {
			thumbsDownUsers.add(user);
		}
	}
	
	public Boolean isThumbsUp() {
		return thumbsUpUsers.contains(SecurityContext.get().getUser());
	}
	
	public Boolean isThumbsDown() {
		return thumbsDownUsers.contains(SecurityContext.get().getUser());
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
	public List<User> getThumbsUpUsers() {
		return thumbsUpUsers;
	}
	
	/**
	 * @return the thumbsDownUsers
	 */
	public List<User> getThumbsDownUsers() {
		return thumbsDownUsers;
	}
	
	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MediaSelection another) {
		Integer thumbsUp = thumbsUpUsers.size();
		Integer anotherThumbsUp = another.thumbsUpUsers.size();
		Integer thumbsDown = thumbsDownUsers.size();
		Integer anotherThumbsDown = another.thumbsDownUsers.size();
		if (getId() == null) {
			return -1;
		} else if (another.getId() == null) {
			return 1;
		} else {
			int comp = anotherThumbsUp.compareTo(thumbsUp);
			if (comp == 0) {
				comp = thumbsDown.compareTo(anotherThumbsDown);
			}
			return comp;
		}
	}
}
