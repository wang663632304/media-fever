package com.mediafever.domain.session;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.User;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSession extends Entity implements Comparable<MediaSession> {
	
	private Boolean expired;
	private Date date;
	private Date time;
	private List<MediaSessionUser> mediaSessionUsers;
	private List<WatchableType> watchableTypes;
	private Set<WatchableType> requiredWatchableTypes;
	private Boolean accepted;
	private List<MediaSelection> selections;
	
	public MediaSession(Long id, Boolean expired, Date date, Date time, List<MediaSessionUser> users,
			List<MediaSelection> selections, List<WatchableType> watchableTypes, Boolean accepted) {
		super(id);
		this.expired = expired;
		this.date = date;
		this.time = time;
		mediaSessionUsers = users;
		setSelections(selections);
		this.watchableTypes = watchableTypes;
		this.accepted = accepted;
		
		requiredWatchableTypes = Sets.newHashSet();
		for (MediaSelection mediaSelection : selections) {
			WatchableType watchableType = WatchableType.find(mediaSelection.getWatchable());
			requiredWatchableTypes.add(watchableType);
		}
	}
	
	public MediaSession(Watchable watchable) {
		expired = false;
		date = DateUtils.now();
		mediaSessionUsers = Lists.newArrayList(new MediaSessionUser(SecurityContext.get().getUser()));
		watchableTypes = Lists.newArrayList(WatchableType.MOVIE);
		requiredWatchableTypes = Sets.newHashSet();
		setSelections(null);
		
		if (watchable != null) {
			addSelection(new MediaSelection(watchable));
		}
	}
	
	public Boolean isExpired() {
		return expired;
	}
	
	public void setSelections(List<MediaSelection> mediaSelections) {
		selections = Lists.newArrayList();
		if (!expired) {
			selections.add(new MediaSelection());
		}
		if (mediaSelections != null) {
			selections.addAll(mediaSelections);
		}
	}
	
	public void thumbsUp(Watchable watchable) {
		MediaSelection mediaSelection = findMediaSelection(watchable);
		mediaSelection.thumbsUp();
	}
	
	public void thumbsDown(Watchable watchable) {
		MediaSelection mediaSelection = findMediaSelection(watchable);
		mediaSelection.thumbsDown();
	}
	
	private MediaSelection findMediaSelection(Watchable watchable) {
		for (MediaSelection mediaSelection : selections) {
			if ((mediaSelection.getWatchable() != null) && mediaSelection.getWatchable().equals(watchable)) {
				return mediaSelection;
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
	
	public List<MediaSessionUser> getMediaSessionUsers() {
		return mediaSessionUsers;
	}
	
	public List<MediaSessionUser> getAcceptedMediaSessionUsers() {
		List<MediaSessionUser> acceptedMediaSessionUsers = Lists.newArrayList();
		for (MediaSessionUser each : mediaSessionUsers) {
			if ((each.isAccepted() != null) && each.isAccepted()) {
				acceptedMediaSessionUsers.add(each);
			}
		}
		return acceptedMediaSessionUsers;
	}
	
	public List<User> getUsers() {
		List<User> users = Lists.newArrayList();
		for (MediaSessionUser each : mediaSessionUsers) {
			users.add(each.getUser());
		}
		return users;
	}
	
	public List<WatchableType> getWatchableTypes() {
		return watchableTypes;
	}
	
	public Boolean acceptOnlyMovies() {
		return (watchableTypes.size() == 1) && watchableTypes.contains(WatchableType.MOVIE);
	}
	
	public Boolean acceptOnlySeries() {
		return (watchableTypes.size() == 1) && watchableTypes.contains(WatchableType.SERIES);
	}
	
	public void accept() {
		accepted = true;
	}
	
	public Boolean isPending() {
		return accepted == null;
	}
	
	public Boolean isActive() {
		return isAccepted() && !isExpired();
	}
	
	public Boolean isAccepted() {
		return (accepted != null) && accepted;
	}
	
	public List<MediaSelection> getSelections() {
		Collections.sort(selections);
		return selections;
	}
	
	public MediaSelection getMediaSelectionWinner() {
		return !selections.isEmpty() ? getSelections().get(0) : null;
	}
	
	public List<MediaSelection> getTop3Selections() {
		List<MediaSelection> mediaSelections = getSelections();
		List<MediaSelection> top3MediaSelections = Lists.newArrayList();
		for (MediaSelection mediaSelection : mediaSelections) {
			if (mediaSelection.getWatchable() != null) {
				if (top3MediaSelections.size() < 3) {
					top3MediaSelections.add(mediaSelection);
				} else {
					break;
				}
			}
		}
		return top3MediaSelections;
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
		mediaSessionUsers.add(new MediaSessionUser(user));
	}
	
	public void removeUser(UserImpl user) {
		MediaSessionUser userToRemove = null;
		for (MediaSessionUser mediaSessionUser : mediaSessionUsers) {
			if (mediaSessionUser.getUser().equals(user)) {
				userToRemove = mediaSessionUser;
				break;
			}
		}
		mediaSessionUsers.remove(userToRemove);
	}
	
	public Boolean containsUser(UserImpl user) {
		for (MediaSessionUser mediaSessionUser : mediaSessionUsers) {
			if (mediaSessionUser.getUser().equals(user)) {
				return true;
			}
		}
		return false;
	}
	
	public void addSelection(MediaSelection mediaSelection) {
		selections.add(mediaSelection);
		WatchableType watchableType = WatchableType.find(mediaSelection.getWatchable());
		requiredWatchableTypes.add(watchableType);
	}
	
	public void removeSelection(MediaSelection mediaSelection) {
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
	
	public void setMediaSessionUsers(List<MediaSessionUser> mediaSessionUsers) {
		this.mediaSessionUsers = mediaSessionUsers;
	}
	
	public void setWatchableTypes(List<WatchableType> watchableTypes) {
		this.watchableTypes = watchableTypes;
	}
	
	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}
	
	public Boolean isWatchableTypeRequired(WatchableType watchableType) {
		return requiredWatchableTypes.contains(watchableType);
	}
	
	/**
	 * @param expired the expired to set
	 */
	public void setExpired(Boolean expired) {
		this.expired = expired;
	}
}
