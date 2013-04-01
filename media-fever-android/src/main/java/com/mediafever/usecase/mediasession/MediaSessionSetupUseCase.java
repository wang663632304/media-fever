package com.mediafever.usecase.mediasession;

import java.util.Date;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionSetupUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private MediaSession mediaSession;
	private Boolean created;
	
	@Inject
	public MediaSessionSetupUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (mediaSession.getId() != null) {
			getApiService().updateMediaSession(mediaSession);
			mediaSessionsRepository.update(mediaSession);
			created = false;
		} else {
			mediaSession = getApiService().createMediaSession(mediaSession);
			mediaSessionsRepository.add(mediaSession);
			created = true;
		}
	}
	
	public void setMediaSession(MediaSession mediaSession) {
		this.mediaSession = mediaSession;
	}
	
	public void setDate(Date date) {
		mediaSession.setDate(date);
	}
	
	public void addWatchableType(WatchableType watchableType) {
		mediaSession.addWatchableType(watchableType);
	}
	
	public void removeWatchableType(WatchableType watchableType) {
		mediaSession.removeWatchableType(watchableType);
	}
	
	public void addUser(UserImpl user) {
		mediaSession.addUser(user);
	}
	
	public void removeUser(UserImpl user) {
		mediaSession.removeUser(user);
	}
	
	public Boolean containsUser(UserImpl user) {
		return mediaSession.containsUser(user);
	}
	
	public MediaSession getMediaSession() {
		return mediaSession;
	}
	
	public void setTime(Date time) {
		mediaSession.setTime(time);
	}
	
	public Boolean isCreated() {
		return created;
	}
	
	public void addMediaSelection(MediaSelection mediaSelection) {
		mediaSession.addSelection(mediaSelection);
	}
}
