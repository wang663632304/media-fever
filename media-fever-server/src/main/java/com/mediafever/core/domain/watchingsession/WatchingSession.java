package com.mediafever.core.domain.watchingsession;

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
public class WatchingSession extends Entity {
	
	private Date date;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "watchingSessionId")
	private List<WatchingSessionUser> users;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "watchingSessionId")
	private List<WatchingSelection> selections;
	
	@ElementCollection
	@JoinTable(name = "WatchingSession_WatchableType", joinColumns = @JoinColumn(name = "watchingSessionId"))
	@Column(name = "watchableType")
	@Enumerated(value = EnumType.STRING)
	private List<WatchableType> watchableTypes;
	
	/**
	 * Default constructor.
	 */
	@SuppressWarnings("unused")
	private WatchingSession() {
		// Do nothing, is required by hibernate
	}
	
	public WatchingSession(List<WatchableType> watchableTypes, Date date) {
		this.watchableTypes = watchableTypes;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public List<WatchingSessionUser> getUsers() {
		return users;
	}
	
	public List<WatchingSelection> getSelections() {
		return selections;
	}
	
	public List<WatchableType> getWatchableTypes() {
		return watchableTypes;
	}
	
}
