package com.mediafever.domain.session;

import java.util.Date;
import java.util.List;
import com.jdroid.android.domain.Entity;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSession extends Entity {
	
	private Date date;
	private Date time;
	private List<MediaSessionUser> users;
	private List<WatchableType> watchableTypes;
	private Boolean accepted;
	private List<MediaSelection> selections;
	
	public MediaSession(Long id, Date date, Date time, List<MediaSessionUser> users,
			List<WatchableType> watchableTypes, Boolean accepted) {
		super(id);
		this.date = date;
		this.time = time;
		this.users = users;
		this.watchableTypes = watchableTypes;
		this.accepted = accepted;
	}
	
	public MediaSession(Date date, Date time, List<WatchableType> watchableTypes, List<MediaSessionUser> users,
			List<MediaSelection> selections) {
		this.date = date;
		this.time = time;
		this.users = users;
		this.watchableTypes = watchableTypes;
		this.selections = selections;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Date getTime() {
		return time;
	}
	
	public List<MediaSessionUser> getUsers() {
		return users;
	}
	
	public List<WatchableType> getWatchableTypes() {
		return watchableTypes;
	}
	
	public Boolean isAccepted() {
		return (accepted != null) && accepted;
	}
	
	public List<MediaSelection> getSelections() {
		return selections;
	}
}
