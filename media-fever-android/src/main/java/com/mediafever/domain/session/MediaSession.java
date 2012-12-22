package com.mediafever.domain.session;

import java.util.Date;
import java.util.List;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.Entity;
import com.jdroid.android.domain.User;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.watchable.Movie;
import com.mediafever.domain.watchable.Watchable;
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
	
	public MediaSession() {
		date = DateUtils.now();
		users = Lists.newArrayList(new MediaSessionUser(SecurityContext.get().getUser()));
		watchableTypes = Lists.newArrayList(WatchableType.MOVIE);
		selections = Lists.newArrayList(new MediaSelection());
		
		// TODO Mocked data
		User user = SecurityContext.get().getUser();
		selections.add(new MediaSelection(new Movie(1L, "Aquí Entre Nos", null,
				"http://cf2.imgobject.com/t/p/w92/eh8LW9tqihgQx1raVmrxnXkjMJ4.jpg", null, null, null, null, null),
				new UserImpl("a", "p", "John", "Locke", null, true), null, null));
		selections.add(new MediaSelection(new Movie(2L, "Pasanga", null,
				"http://cf2.imgobject.com/t/p/w92/7X7eNyxKG44MXfOjtERPSnPbvPK.jpg", null, null, null, null, null),
				user, null, null));
		selections.add(new MediaSelection(new Movie(3L, "Les Enfoirés 2012 - Le Bal des Enfoirés", null,
				"http://cf2.imgobject.com/t/p/w92/yyMqWI6u3ssCmJudQZsUCkw8nuz.jpg", null, null, null, null, null),
				user, null, null));
		selections.add(new MediaSelection(new Movie(4L, "Oar", null,
				"http://cf2.imgobject.com/t/p/w92/vlZXD0zjDk6tUA4TLePvYX5Bw1R.jpg", null, null, null, null, null),
				user, null, null));
		selections.add(new MediaSelection(new Movie(5L, "Blek Giek", null,
				"http://cf2.imgobject.com/t/p/w92/d7osS0KQhmuU2RA6nXNeJ7iUWHB.jpg", null, null, null, null, null),
				user, null, null));
		
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
	
	public void addSelection(MediaSelection selection) {
		selections.add(selection);
	}
	
}
