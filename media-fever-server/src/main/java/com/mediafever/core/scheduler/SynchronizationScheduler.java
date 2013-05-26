package com.mediafever.core.scheduler;

import java.util.Date;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.service.SynchronizationService;
import com.mediafever.core.service.SynchronizationService.SynchronizationListener;

/**
 * Scheduler that handles {@link Movie}s and {@link Series} synchronization processes. It's responsible for handling
 * both scheduled jobs and manually triggered jobs while avoiding duplicate sync processes to run at a same instance of
 * time.
 * 
 * @author Estefanía Caravatti
 */
@Component
public class SynchronizationScheduler implements SynchronizationListener {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(SynchronizationScheduler.class);
	
	@Autowired
	private SynchronizationService synchronizationService;
	
	private SyncController seriesSyncController = new SyncController();
	private SyncController moviesSyncController = new SyncController();
	
	public String getLastSeriesSyncEndDate() {
		return getLastSyncEndDate(seriesSyncController);
	}
	
	public String getLastSeriesSyncStartDate() {
		return getLastSyncStartDate(seriesSyncController);
	}
	
	public String getLastMoviesSyncEndDate() {
		return getLastSyncEndDate(moviesSyncController);
	}
	
	public String getLastMoviesSyncStartDate() {
		return getLastSyncStartDate(moviesSyncController);
	}
	
	public String getLastSyncEndDate(SyncController syncController) {
		return DateUtils.format(syncController.getLastSyncEndDate(), DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT);
	}
	
	public String getLastSyncStartDate(SyncController syncController) {
		return DateUtils.format(syncController.getLastSyncStartDate(), DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT);
	}
	
	/**
	 * Executes a series synchronization process with a fixed period of 10 hours between the end of the last invocation
	 * and the start of the next one.
	 */
	@Scheduled(cron = "${cron.sync.series}")
	public void scheduledSeriesSync() {
		if (startSyncSeriesSchedule()) {
			LOGGER.info("Series synchronization process started.");
		} else {
			LOGGER.info("Series synchronization already in progress, skipping this schedule.");
		}
	}
	
	/**
	 * Executes a movies synchronization process with a fixed period of 10 hours between the end of the last invocation
	 * and the start of the next one.
	 */
	@Scheduled(cron = "${cron.sync.movies}")
	public void scheduledMoviesSync() {
		if (startSyncMoviesSchedule()) {
			LOGGER.info("Movies synchronization process started.");
		} else {
			LOGGER.info("Movies synchronization already in progress, skipping this schedule.");
		}
	}
	
	/**
	 * Starts an async process to synchronize series.
	 * 
	 * @return TRUE if the sync was started or FALSE if a sync was already in progress.
	 */
	public Boolean startSyncSeriesSchedule() {
		if (seriesSyncController.acquireLock()) {
			synchronizationService.synchSeries(this);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * Starts an async process to synchronize movies.
	 * 
	 * @return TRUE if the sync was started or FALSE if a sync was already in progress.
	 */
	public Boolean startSyncMoviesSchedule() {
		if (moviesSyncController.acquireLock()) {
			synchronizationService.synchMovies(this);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * @see com.mediafever.core.service.SynchronizationService.SynchronizationListener#onSyncMoviesFinished()
	 */
	@Override
	public void onSyncMoviesFinished() {
		moviesSyncController.releaseLock();
	}
	
	/**
	 * @see com.mediafever.core.service.SynchronizationService.SynchronizationListener#onSyncSeriesFinished()
	 */
	@Override
	public void onSyncSeriesFinished() {
		seriesSyncController.releaseLock();
	}
	
	/**
	 * SyncController used to acquire/release a lock to handle synchronized access to the sync process of a
	 * {@link Watchable}.
	 * 
	 * @author Estefanía Caravatti
	 */
	private class SyncController {
		
		private Boolean syncInProgress = Boolean.FALSE;
		private Date syncStartDate;
		private Date syncEndDate;
		
		public synchronized Boolean acquireLock() {
			if (!syncInProgress) {
				syncInProgress = Boolean.TRUE;
				syncStartDate = new Date();
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		
		public synchronized void releaseLock() {
			syncInProgress = Boolean.FALSE;
			syncEndDate = new Date();
		}
		
		public Date getLastSyncStartDate() {
			return syncStartDate;
		}
		
		public Date getLastSyncEndDate() {
			return syncEndDate;
		}
		
	}
}
