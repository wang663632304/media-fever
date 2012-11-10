package com.mediafever.usecase;

import java.util.Date;
import java.util.List;
import com.google.inject.Inject;
import com.jdroid.android.usecase.AbstractApiUseCase;
import com.jdroid.java.collections.Lists;
import com.mediafever.domain.session.MediaSession;
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
	private MediaSession mediaSession;
	
	@Inject
	public MediaSessionSetupUseCase(APIService apiService) {
		super(apiService);
	}
	
	/**
	 * @see com.jdroid.android.usecase.DefaultAbstractUseCase#doExecute()
	 */
	@Override
	protected void doExecute() {
		mediaSession = new MediaSession(date, watchableTypes);
		// TODO Implement API call here
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
	
	public MediaSession getMediaSession() {
		return mediaSession;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
}
