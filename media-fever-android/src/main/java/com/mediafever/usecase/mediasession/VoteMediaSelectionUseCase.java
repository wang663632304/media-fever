package com.mediafever.usecase.mediasession;

import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.service.APIService;

/**
 * 
 * @author Maxi Rosson
 */
public class VoteMediaSelectionUseCase extends AbstractApiUseCase<APIService> {
	
	private MediaSession mediaSession;
	private MediaSelection mediaSelection;
	private Boolean thumbsUp;
	
	@Inject
	public VoteMediaSelectionUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		getApiService().voteMediaSelection(mediaSelection, thumbsUp);
		
		if (thumbsUp) {
			mediaSession.thumbsUp(mediaSelection.getWatchable());
		} else {
			mediaSession.thumbsDown(mediaSelection.getWatchable());
		}
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
	
	/**
	 * @param mediaSession the mediaSession to set
	 */
	public void setMediaSession(MediaSession mediaSession) {
		this.mediaSession = mediaSession;
	}
	
}
