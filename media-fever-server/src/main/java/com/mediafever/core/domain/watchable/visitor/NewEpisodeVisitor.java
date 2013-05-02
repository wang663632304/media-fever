package com.mediafever.core.domain.watchable.visitor;

import java.util.List;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jdroid.javaweb.guava.function.NestedPropertyFunction;
import com.jdroid.javaweb.push.PushService;
import com.jdroid.javaweb.push.gcm.GcmMessage;
import com.mediafever.core.domain.UserWatchable;
import com.mediafever.core.domain.watchable.Episode;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.domain.watchable.WatchableType;
import com.mediafever.core.repository.UserWatchableRepository;
import com.mediafever.core.service.push.gcm.NewEpisodeGcmMessage;

/**
 * Visitor that sends a new episode {@link GcmMessage} to users watching a series when a new episode is released.
 * 
 * @author Estefanía Caravatti
 */
public class NewEpisodeVisitor implements WatchableVisitor {
	
	private UserWatchableRepository userWatchableRepository;
	
	private PushService pushService;
	
	private Long seriesExternalId;
	
	private String seriesName;
	
	private String seriesImageURL;
	
	public NewEpisodeVisitor(Long seriesExternalId, String seriesName, String seriesImageURL,
			UserWatchableRepository userWatchableRepository, PushService pushService) {
		this.seriesExternalId = seriesExternalId;
		this.seriesImageURL = seriesImageURL;
		this.seriesName = seriesName;
		this.userWatchableRepository = userWatchableRepository;
		this.pushService = pushService;
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.visitor.WatchableVisitor#visitNew(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void visitNew(Watchable watchable) {
		// This visitor only works for Episodes.
		if (watchable instanceof Episode) {
			
			Episode episode = Episode.class.cast(watchable);
			List<UserWatchable> userWatchables = userWatchableRepository.getWatchedBy(seriesExternalId,
				WatchableType.SERIES);
			
			if (!userWatchables.isEmpty()) {
				// Get all the users that watch the series
				List<Long> userIds = Lists.transform(userWatchables, new NestedPropertyFunction<UserWatchable, Long>(
						"user.id"));
				
				// Get the watchable's id from any of the userWatchables retrieved.
				Long watchableId = Iterables.getFirst(userWatchables, null).getWatchable().getId();
				
				pushService.send(
					new NewEpisodeGcmMessage(watchableId, seriesName, episode.getName(), episode.getEpisodeNumber(),
							episode.getImageURL() != null ? episode.getImageURL() : seriesImageURL), userIds);
			}
		}
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.visitor.WatchableVisitor#visitUpdated(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void visitUpdated(Watchable watchable) {
		// No behavior for this scenario.
	}
	
	/**
	 * @see com.mediafever.core.domain.watchable.visitor.WatchableVisitor#visitDeleted(com.mediafever.core.domain.watchable.Watchable)
	 */
	@Override
	public void visitDeleted(Watchable watchable) {
		// No behavior for this scenario.
	}
}
