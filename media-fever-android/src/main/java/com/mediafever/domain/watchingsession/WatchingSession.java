package com.mediafever.domain.watchingsession;

import java.util.Date;
import java.util.List;
import com.jdroid.android.domain.Entity;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSession extends Entity {
	
	private Date date;
	private List<WatchingSessionUser> users;
	private List<WatchableType> watchableTypes;
	private Boolean accepted;
	
	public WatchingSession(Long id, Date date, List<WatchingSessionUser> users, List<WatchableType> watchableTypes,
			Boolean accepted) {
		super(id);
		this.date = date;
		this.users = users;
		this.watchableTypes = watchableTypes;
		this.accepted = accepted;
	}
	
	public Date getDate() {
		return date;
	}
	
	public List<WatchingSessionUser> getUsers() {
		return users;
	}
	
	public List<WatchableType> getWatchableTypes() {
		return watchableTypes;
	}
	
	public Boolean isAccepted() {
		return accepted;
	}
	
}
