package com.mediafever.core.domain.watchingsession;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import com.jdroid.javaweb.domain.Entity;
import com.mediafever.core.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class WatchingSelection extends Entity {
	
	@OneToOne
	@JoinColumn(name = "watchableId", nullable = false)
	private Watchable watchable;
	
	@OneToMany(targetEntity = WatchingSessionUser.class, fetch = FetchType.LAZY, orphanRemoval = true,
			cascade = CascadeType.ALL)
	@JoinTable(name = "WatchingSelection_ThumbsUpUsers", joinColumns = @JoinColumn(name = "watchingSelectionId"),
			inverseJoinColumns = @JoinColumn(name = "watchingSessionUserId"))
	private List<WatchingSessionUser> thumbsUpUsers;
	
	@OneToMany(targetEntity = WatchingSessionUser.class, fetch = FetchType.LAZY, orphanRemoval = true,
			cascade = CascadeType.ALL)
	@JoinTable(name = "WatchingSelection_ThumbsDownUsers", joinColumns = @JoinColumn(name = "watchingSelectionId"),
			inverseJoinColumns = @JoinColumn(name = "watchingSessionUserId"))
	private List<WatchingSessionUser> thumbsDownUsers;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private WatchingSelection() {
		// Do nothing, is required by hibernate
	}
	
	public WatchingSelection(Watchable watchable) {
		this.watchable = watchable;
	}
	
	public Watchable getWatchable() {
		return watchable;
	}
	
	public List<WatchingSessionUser> getThumbsUpUsers() {
		return thumbsUpUsers;
	}
	
	public List<WatchingSessionUser> getThumbsDownUsers() {
		return thumbsDownUsers;
	}
	
}
