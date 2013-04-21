package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.repository.MediaSessionsRepository;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class AddManualSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private Watchable watchable;
	private MediaSessionsRepository mediaSessionsRepository;
	private Long mediaSessionId;
	private MediaSelection mediaSelection;
	
	@Inject
	public AddManualSelectionUseCase(APIService apiService, MediaSessionsRepository mediaSessionsRepository) {
		super(apiService);
		this.mediaSessionsRepository = mediaSessionsRepository;
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		MediaSession mediaSession = mediaSessionsRepository.get(mediaSessionId);
		mediaSelection = getApiService().addManualSelection(mediaSession, watchable);
		mediaSession.addSelection(mediaSelection);
		mediaSessionsRepository.update(mediaSession);
	}
	
	/**
	 * @return the mediaSelection
	 */
	public MediaSelection getMediaSelection() {
		return mediaSelection;
	}
	
	public void setMediaSessionId(Long mediaSessionId) {
		this.mediaSessionId = mediaSessionId;
	}
	
	/**
	 * @param watchable the watchable to set
	 */
	public void setWatchable(Watchable watchable) {
		this.watchable = watchable;
	}
	
}
