package com.mediafever.core.scheduler;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.javaweb.guava.function.NestedPropertyFunction;
import com.jdroid.javaweb.push.PushService;
import com.mediafever.core.domain.UserWatchable;
import com.mediafever.core.domain.watchable.Episode;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.repository.UserWatchableRepository;
import com.mediafever.core.repository.WatchableRepository;
import com.mediafever.core.service.push.gcm.NewEpisodeGcmMessage;

/**
 * Scheduler to send push notifications to all users that watch a series when a new episode is released.
 * 
 * @author Estefanía Caravatti
 */
@Component
public class NewEpisodeScheduler {
	
	@Autowired
	private WatchableRepository watchableRepository;
	
	@Autowired
	private UserWatchableRepository userWatchableRepository;
	
	@Autowired
	private PushService pushService;
	
	@Scheduled(cron = "${cron.notifications.newEpisode}")
	@Transactional
	public void notifyNewEpisodes() {
		
		Date date = DateUtils.now();
		List<Series> seriesWithReleasedEpisodes = watchableRepository.getSeriesWithReleasedEpisodes(date);
		
		for (Series series : seriesWithReleasedEpisodes) {
			List<UserWatchable> userWatchables = userWatchableRepository.getWatchedBy(series);
			
			if (!userWatchables.isEmpty()) {
				// Get all the users that watch the series
				List<Long> userIds = Lists.transform(userWatchables, new NestedPropertyFunction<UserWatchable, Long>(
						"user.id"));
				
				for (Episode episode : series.getReleasedEpisodes(date)) {
					
					pushService.send(
						new NewEpisodeGcmMessage(series.getId(), series.getName(), episode.getName(),
								episode.getEpisodeNumber(), episode.getImageURL() != null ? episode.getImageURL()
										: series.getImageURL()), userIds);
				}
			}
		}
	}
}
