package com.mediafever.usecase.mediasession;

import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionsUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private Long userId;
	
	@Inject
	public MediaSessionsUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		if (mediaSessionsRepository.isOutdated()) {
			List<MediaSession> mediaSessions = getApiService().getMediaSessions(userId);
			mediaSessionsRepository.replaceAll(mediaSessions);
			mediaSessionsRepository.refreshUpdateTimestamp();
		}
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public List<MediaSession> getPendingMediaSessions() {
		return mediaSessionsRepository.getPendingMediaSessions();
	}
	
	public List<MediaSession> getAcceptedMediaSessions() {
		return mediaSessionsRepository.getAcceptedMediaSessions();
	}
}
