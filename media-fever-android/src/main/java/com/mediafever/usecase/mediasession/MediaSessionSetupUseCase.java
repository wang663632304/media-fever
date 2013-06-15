package com.mediafever.usecase.mediasession;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.domain.User;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.session.MediaSessionUser;
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
	
	private List<User> acceptedUsers;
	private List<User> pendingUsers;
	
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
		if (pendingUsers == null) {
			pendingUsers = Lists.newArrayList();
			acceptedUsers = Lists.newArrayList();
			for (MediaSessionUser mediaSessionUser : mediaSession.getMediaSessionUsers()) {
				if (mediaSessionUser.isAccepted() == null) {
					pendingUsers.add(mediaSessionUser.getUser());
				} else {
					acceptedUsers.add(mediaSessionUser.getUser());
				}
			}
		}
	}
	
	public Boolean isRemovableUser(UserImpl user) {
		if (mediaSession.getId() != null) {
			return !pendingUsers.contains(user) && !acceptedUsers.contains(user);
		} else {
			return true;
		}
	}
	
	public Boolean isAcceptedUser(UserImpl user) {
		if (mediaSession.getId() != null) {
			return acceptedUsers.contains(user);
		} else {
			return false;
		}
	}
	
	public Boolean isPendingUser(UserImpl user) {
		if (mediaSession.getId() != null) {
			return pendingUsers.contains(user);
		} else {
			return false;
		}
	}
	
	public MediaSession getMediaSession() {
		return mediaSession;
	}
	
	public Boolean isCreated() {
		return created;
	}
}
