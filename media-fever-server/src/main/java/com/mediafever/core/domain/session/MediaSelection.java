package com.mediafever.core.domain.session;

import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import com.jdroid.javaweb.domain.Entity;
import com.mediafever.core.domain.User;
import com.mediafever.core.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class MediaSelection extends Entity {
	
	@OneToOne
	@JoinColumn(name = "ownerId", nullable = false)
	private User owner;
	
	@OneToOne
	@JoinColumn(name = "watchableId", nullable = false)
	private Watchable watchable;
	
	@OneToMany(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinTable(name = "MediaSelection_ThumbsUpUsers", joinColumns = @JoinColumn(name = "mediaSelectionId"),
			inverseJoinColumns = @JoinColumn(name = "userId"))
	private List<User> thumbsUpUsers;
	
	@OneToMany(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinTable(name = "MediaSelection_ThumbsDownUsers", joinColumns = @JoinColumn(name = "mediaSelectionId"),
			inverseJoinColumns = @JoinColumn(name = "userId"))
	private List<User> thumbsDownUsers;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private MediaSelection() {
		// Do nothing, is required by hibernate
	}
	
	public MediaSelection(Watchable watchable) {
		this.watchable = watchable;
	}
	
	public void thumbsUp(User user) {
		thumbsDownUsers.remove(user);
		if (!thumbsUpUsers.contains(user)) {
			thumbsUpUsers.add(user);
		}
	}
	
	public void thumbsDown(User user) {
		thumbsUpUsers.remove(user);
		if (!thumbsDownUsers.contains(user)) {
			thumbsDownUsers.add(user);
		}
	}
	
	public Watchable getWatchable() {
		return watchable;
	}
	
	public List<User> getThumbsUpUsers() {
		return thumbsUpUsers;
	}
	
	public List<User> getThumbsDownUsers() {
		return thumbsDownUsers;
	}
	
	public User getOwner() {
		return owner;
	}
	
}
