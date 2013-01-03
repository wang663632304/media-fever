package com.mediafever.domain.session;

import java.util.Date;
import java.util.List;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.User;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSession extends Entity implements Comparable<MediaSession> {
	
	private Date date;
	private Date time;
	private List<MediaSessionUser> users;
	private List<WatchableType> watchableTypes;
	private Boolean accepted;
	private List<MediaSelection> selections;
	
	public MediaSession(Long id, Date date, Date time, List<MediaSessionUser> users, List<MediaSelection> selections,
			List<WatchableType> watchableTypes, Boolean accepted) {
		super(id);
		this.date = date;
		this.time = time;
		this.users = users;
		setSelections(selections);
		this.watchableTypes = watchableTypes;
		this.accepted = accepted;
	}
	
	public MediaSession() {
		date = DateUtils.now();
		users = Lists.newArrayList(new MediaSessionUser(SecurityContext.get().getUser()));
		watchableTypes = Lists.newArrayList(WatchableType.MOVIE);
		selections = Lists.newArrayList(new MediaSelection());
	}
	
	public void thumbsUp(Watchable watchable) {
		MediaSelection mediaSelection = findMediaSelection(watchable);
		MediaSessionUser mediaSessionUser = getMe();
		mediaSelection.thumbsUp(mediaSessionUser);
		mediaSessionUser.decrementPendingThumbsUp();
	}
	
	public void thumbsDown(Watchable watchable) {
		MediaSelection mediaSelection = findMediaSelection(watchable);
		MediaSessionUser mediaSessionUser = getMe();
		mediaSelection.thumbsDown(mediaSessionUser);
		mediaSessionUser.decrementPendingThumbsDown();
	}
	
	private MediaSelection findMediaSelection(Watchable watchable) {
		for (MediaSelection mediaSelection : selections) {
			if ((mediaSelection.getWatchable() != null) && mediaSelection.getWatchable().equals(watchable)) {
				return mediaSelection;
			}
		}
		return null;
	}
	
	public MediaSessionUser getMe() {
		User user = SecurityContext.get().getUser();
		for (MediaSessionUser mediaUser : users) {
			if (mediaUser.getUser().equals(user)) {
				return mediaUser;
			}
		}
		return null;
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
	
	public void accept() {
		accepted = true;
	}
	
	public Boolean isAccepted() {
		return (accepted != null) && accepted;
	}
	
	public List<MediaSelection> getSelections() {
		return selections;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public void addWatchableType(WatchableType watchableType) {
		watchableTypes.add(watchableType);
	}
	
	public void removeWatchableType(WatchableType watchableType) {
		watchableTypes.remove(watchableType);
	}
	
	public void addUser(UserImpl user) {
		users.add(new MediaSessionUser(user));
	}
	
	public void removeUser(UserImpl user) {
		MediaSessionUser userToRemove = null;
		for (MediaSessionUser mediaSessionUser : users) {
			if (mediaSessionUser.getUser().equals(user)) {
				userToRemove = mediaSessionUser;
				break;
			}
		}
		users.remove(userToRemove);
	}
	
	public Boolean containsUser(UserImpl user) {
		for (MediaSessionUser mediaSessionUser : users) {
			if (mediaSessionUser.getUser().equals(user)) {
				return true;
			}
		}
		return false;
	}
	
	public void addSelection(Watchable watchable) {
		selections.add(new MediaSelection(watchable));
	}
	
	public void removeSelection(Watchable watchable) {
		MediaSelection mediaSelection = findMediaSelection(watchable);
		selections.remove(mediaSelection);
	}
	
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MediaSession another) {
		int result = 0;
		
		// Order example:
		// AnyDate AnyTime
		// AnyDate 14:00
		// AnyDate 23:30
		// Today AnyTime
		// Today 23:30
		// Tomorrow AnyTime
		// Tomorrow 23:30
		
		if ((date == null) && (another.date != null)) {
			result = -1;
		} else if ((date != null) && (another.date == null)) {
			result = 1;
		} else if ((date != null) && (another.date != null)) {
			Date date1 = DateUtils.truncate(date);
			Date date2 = DateUtils.truncate(another.date);
			result = date1.compareTo(date2);
		}
		
		if (result == 0) {
			if ((time == null) && (another.time != null)) {
				result = -1;
			} else if ((time != null) && (another.time == null)) {
				result = 1;
			} else if ((time != null) && (another.time != null)) {
				Date now = DateUtils.now();
				Date time1 = DateUtils.getDate(now, time);
				Date time2 = DateUtils.getDate(now, another.time);
				result = time1.compareTo(time2);
			}
		}
		return result;
	}
	
	public void setUsers(List<MediaSessionUser> users) {
		this.users = users;
	}
	
	public void setWatchableTypes(List<WatchableType> watchableTypes) {
		this.watchableTypes = watchableTypes;
	}
	
	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}
	
	public void setSelections(List<MediaSelection> selections) {
		this.selections = Lists.newArrayList(new MediaSelection());
		if (selections != null) {
			this.selections.addAll(selections);
		}
	}
}
