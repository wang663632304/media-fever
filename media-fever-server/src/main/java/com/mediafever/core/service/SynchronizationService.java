package com.mediafever.core.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.domain.watchable.Settings;
import com.mediafever.core.repository.PeopleRepository;
import com.mediafever.core.repository.SettingsRepository;
import com.mediafever.core.repository.WatchableRepository;
import com.mediafever.core.service.moviedb.MovieDbApiService;
import com.mediafever.core.service.tvdb.TVDbApiService;
import com.mediafever.core.service.tvdb.parser.SeriesUpdateResponse;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class SynchronizationService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(SynchronizationService.class);
	
	private static final String INITIAL_UPDATE = "1";
	
	private static final int SLEEP_IN_MILLI = 500;
	
	@Autowired
	private MovieDbApiService movieDbApiService;
	
	@Autowired
	private TVDbApiService tvDbApiService;
	
	@Autowired
	private WatchableService watchableService;
	
	@Autowired
	private PeopleRepository peopleRepository;
	
	@Autowired
	private WatchableRepository watchableRepository;
	
	@Autowired
	private SettingsRepository settingsRepository;
	
	/**
	 * Synchronizes the Movies information from the MovieDb API to have the latest content.
	 * 
	 * @param listener {@link SynchronizationListener} that will be notified when the sync process is over.
	 */
	@Transactional
	@Async
	public void synchMovies(SynchronizationListener listener) {
		
		try {
			// Load new movies. Aprox 186587
			Long minExternalId = watchableRepository.getLastMovieExternalId() + 1;
			Long latestExternalId = movieDbApiService.getLatest();
			addMovies(minExternalId, latestExternalId);
			
			// Update old movies that have been updated on the MovieDB.
			for (Long externalId : movieDbApiService.getUpdatedMovieIds()) {
				addMovie(externalId);
			}
		} catch (Exception e) {
			LOGGER.error("Error synchronizing movies", e);
		} finally {
			listener.onSyncMoviesFinished();
		}
		
	}
	
	private void addMovies(Long fromExternalId, Long toExternalId) {
		for (long i = fromExternalId; i <= toExternalId; i++) {
			addMovie(i);
		}
	}
	
	private void addMovie(Long externalId) {
		Movie movie = movieDbApiService.getMovie(externalId, peopleRepository);
		if (movie != null) {
			watchableService.saveWatchable(movie);
		}
		ExecutorUtils.sleepInMillis(SLEEP_IN_MILLI);
	}
	
	/**
	 * Synchronizes the Series information from the TvDb API to have the latest content.
	 * 
	 * @param listener {@link SynchronizationListener} that will be notified when the sync process is over.
	 */
	@Transactional
	@Async
	public void synchSeries(SynchronizationListener listener) {
		
		try {
			Settings lastUpdateSettings = settingsRepository.getSeriesLastUpdateSetting();
			
			SeriesUpdateResponse response;
			if (INITIAL_UPDATE.equals(lastUpdateSettings.getValue())) {
				response = tvDbApiService.getAllSeries();
			} else {
				// TODO: Improve the sync process to avoid updating episodes that haven't been modified.
				response = tvDbApiService.getUpdatedSeries(lastUpdateSettings.getValue());
			}
			
			// Update the DB only if there's a new update.
			if (!lastUpdateSettings.getValue().equals(response.getTime())) {
				
				for (Long seriesId : response.getSeriesIds()) {
					try {
						addSeries(seriesId);
					} catch (Exception e) {
						// TODO: Add retry functionality.
						LOGGER.error("Failed to update series with id " + seriesId, e);
					}
				}
				
				// Update the lastUpdate time in the app settings.
				lastUpdateSettings.update(response.getTime());
				settingsRepository.update(lastUpdateSettings);
			}
			
		} finally {
			listener.onSyncSeriesFinished();
		}
	}
	
	/**
	 * @param seriesId
	 */
	private void addSeries(Long seriesId) {
		Series series = tvDbApiService.getSeries(seriesId, peopleRepository);
		if (series != null) {
			watchableService.saveWatchable(series);
		}
		ExecutorUtils.sleepInMillis(SLEEP_IN_MILLI);
	}
	
	/**
	 * Listener for the synchronization processes.
	 * 
	 * @author Estefanía Caravatti
	 */
	public interface SynchronizationListener {
		
		public void onSyncMoviesFinished();
		
		public void onSyncSeriesFinished();
		
	}
}
