package com.mediafever.usecase.mediasession;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionsUseCase extends AbstractApiUseCase<APIService> {
	
	private Long userId;
	private List<MediaSession> pendingMediaSessions;
	private List<MediaSession> acceptedMediaSessions;
	
	@Inject
	public MediaSessionsUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		pendingMediaSessions = Lists.newArrayList();
		acceptedMediaSessions = Lists.newArrayList();
		List<MediaSession> mediaSessions = getApiService().getMediaSessions(userId);
		for (MediaSession mediaSession : mediaSessions) {
			if (mediaSession.isAccepted()) {
				acceptedMediaSessions.add(mediaSession);
			} else {
				pendingMediaSessions.add(mediaSession);
			}
		}
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<MediaSession> getPendingMediaSessions() {
		return pendingMediaSessions;
	}
	
	public List<MediaSession> getAcceptedMediaSessions() {
		return acceptedMediaSessions;
	}
}
