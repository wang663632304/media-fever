package com.mediafever.core.domain.session;

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
public class MediaSelection extends Entity {
	
	@OneToOne
	@JoinColumn(name = "watchableId", nullable = false)
	private Watchable watchable;
	
	@OneToMany(targetEntity = MediaSessionUser.class, fetch = FetchType.LAZY, orphanRemoval = true,
			cascade = CascadeType.ALL)
	@JoinTable(name = "MediaSelection_ThumbsUpUsers", joinColumns = @JoinColumn(name = "mediaSelectionId"),
			inverseJoinColumns = @JoinColumn(name = "mediaSessionUserId"))
	private List<MediaSessionUser> thumbsUpUsers;
	
	@OneToMany(targetEntity = MediaSessionUser.class, fetch = FetchType.LAZY, orphanRemoval = true,
			cascade = CascadeType.ALL)
	@JoinTable(name = "MediaSelection_ThumbsDownUsers", joinColumns = @JoinColumn(name = "mediaSelectionId"),
			inverseJoinColumns = @JoinColumn(name = "mediaSessionUserId"))
	private List<MediaSessionUser> thumbsDownUsers;
	
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
	
	public Watchable getWatchable() {
		return watchable;
	}
	
	public List<MediaSessionUser> getThumbsUpUsers() {
		return thumbsUpUsers;
	}
	
	public List<MediaSessionUser> getThumbsDownUsers() {
		return thumbsDownUsers;
	}
	
}
