package com.mediafever.core.domain.session;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import com.jdroid.javaweb.domain.Entity;
import com.mediafever.core.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
@javax.persistence.Entity
public class MediaSession extends Entity {
	
	private Date date;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mediaSessionId")
	private List<MediaSessionUser> users;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mediaSessionId")
	private List<MediaSelection> selections;
	
	@ElementCollection
	@JoinTable(name = "MediaSession_WatchableType", joinColumns = @JoinColumn(name = "mediaSessionId"))
	@Column(name = "watchableType")
	@Enumerated(value = EnumType.STRING)
	private List<WatchableType> watchableTypes;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private MediaSession() {
		// Do nothing, is required by hibernate
	}
	
	public MediaSession(List<WatchableType> watchableTypes, Date date) {
		this.watchableTypes = watchableTypes;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public List<MediaSessionUser> getUsers() {
		return users;
	}
	
	public List<MediaSelection> getSelections() {
		return selections;
	}
	
	public List<WatchableType> getWatchableTypes() {
		return watchableTypes;
	}
	
}
