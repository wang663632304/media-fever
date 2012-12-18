package com.mediafever.usecase;

import java.util.Date;
import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.session.MediaSessionUser;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionSetupUseCase extends AbstractApiUseCase<APIService> {
	
	private Date date;
	private Date time;
	private List<WatchableType> watchableTypes = Lists.newArrayList(WatchableType.MOVIE);
	private List<UserImpl> users = Lists.newArrayList();
	private List<MediaSelection> selections = Lists.newArrayList();
	private MediaSession mediaSession;
	
	@Inject
	public MediaSessionSetupUseCase(APIService apiService) {
		super(apiService);
		
		// TODO Mocked data
		User user = SecurityContext.get().getUser();
		selections.add(new MediaSelection(new Watchable(null, "Aquí Entre Nos",
				"http://cf2.imgobject.com/t/p/w92/eh8LW9tqihgQx1raVmrxnXkjMJ4.jpg", null, null, null, null),
				new UserImpl("a", "p", "John", "Locke", null, true), null, null));
		selections.add(new MediaSelection(new Watchable(null, "Pasanga",
				"http://cf2.imgobject.com/t/p/w92/7X7eNyxKG44MXfOjtERPSnPbvPK.jpg", null, null, null, null), user,
				null, null));
		selections.add(new MediaSelection(new Watchable(null, "Les Enfoirés 2012 - Le Bal des Enfoirés",
				"http://cf2.imgobject.com/t/p/w92/yyMqWI6u3ssCmJudQZsUCkw8nuz.jpg", null, null, null, null), user,
				null, null));
		selections.add(new MediaSelection(new Watchable(null, "Oar",
				"http://cf2.imgobject.com/t/p/w92/vlZXD0zjDk6tUA4TLePvYX5Bw1R.jpg", null, null, null, null), user,
				null, null));
		selections.add(new MediaSelection(new Watchable(null, "Blek Giek",
				"http://cf2.imgobject.com/t/p/w92/d7osS0KQhmuU2RA6nXNeJ7iUWHB.jpg", null, null, null, null), user,
				null, null));
		
		selections.add(new MediaSelection());
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		List<MediaSessionUser> mediaSessionUsers = Lists.newArrayList();
		for (UserImpl user : users) {
			mediaSessionUsers.add(new MediaSessionUser(user));
		}
		mediaSession = new MediaSession(date, time, watchableTypes, mediaSessionUsers, selections);
		getApiService().createMediaSession(mediaSession);
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public void addWatchableType(WatchableType watchableType) {
		watchableTypes.add(watchableType);
	}
	
	public void removeWatchableType(WatchableType watchableType) {
		watchableTypes.remove(watchableType);
	}
	
	public void addUser(UserImpl user) {
		users.add(user);
	}
	
	public void removeUser(UserImpl user) {
		users.remove(user);
	}
	
	public Boolean containsUser(UserImpl user) {
		return users.contains(user);
	}
	
	public MediaSession getMediaSession() {
		return mediaSession;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public void addSelection(MediaSelection selection) {
		selections.add(selection);
	}
	
	public List<MediaSelection> getSelections() {
		return selections;
	}
}
