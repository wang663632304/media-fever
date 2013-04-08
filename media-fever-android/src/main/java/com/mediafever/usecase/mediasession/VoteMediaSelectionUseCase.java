package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class VoteMediaSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSessionsRepository mediaSessionsRepository;
	private Long mediaSessionId;
	private MediaSelection mediaSelection;
	private Boolean thumbsUp;
	
	@Inject
	public VoteMediaSelectionUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		
		MediaSession mediaSession = mediaSessionsRepository.get(mediaSessionId);
		if (thumbsUp) {
			getApiService().thumbsUpMediaSelection(mediaSession, mediaSelection);
			mediaSession.thumbsUp(mediaSelection.getWatchable());
		} else {
			getApiService().thumbsDownMediaSelection(mediaSession, mediaSelection);
			mediaSession.thumbsDown(mediaSelection.getWatchable());
		}
		mediaSessionsRepository.update(mediaSession);
	}
	
	public void setThumbsUp(Boolean thumbsUp) {
		this.thumbsUp = thumbsUp;
	}
	
	/**
	 * @param mediaSelection the mediaSelection to set
	 */
	public void setMediaSelection(MediaSelection mediaSelection) {
		this.mediaSelection = mediaSelection;
	}
	
	public void setMediaSessionId(Long mediaSessionId) {
		this.mediaSessionId = mediaSessionId;
	}
	
}
