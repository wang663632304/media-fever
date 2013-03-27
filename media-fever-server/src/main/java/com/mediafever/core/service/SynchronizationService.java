package com.mediafever.core.service;

import java.util.List;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.domain.watchable.Settings;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.repository.PeopleRepository;
import com.mediafever.core.repository.SettingsRepository;
import com.mediafever.core.repository.WatchableRepository;
import com.mediafever.core.service.moviedb.MovieDbApiService;
import com.mediafever.core.service.tvdb.TVDbApiService;
import com.mediafever.core.service.tvdb.parser.SeriesUpdateParser.SeriesUpdateResponse;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class SynchronizationService {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(SynchronizationService.class);
	
	private static final int SLEEP = 2;
	
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
	
	public void synchMovies() {
		// Load new movies
		Long minExternalId = watchableRepository.getLastMovieExternalId() + 1;
		Long latestExternalId = movieDbApiService.getLatest();
		addMovies(minExternalId, latestExternalId);
		
		// Update old Movies
		Integer page = 1;
		PagedResult<Watchable> pagedResult = null;
		do {
			pagedResult = watchableRepository.getMovies(page);
			List<Long> externalIds = movieDbApiService.getVersion(pagedResult.getData());
			for (Long externalId : externalIds) {
				addMovie(externalId);
			}
			page++;
		} while (!pagedResult.isLastPage());
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
		ExecutorUtils.sleep(SLEEP);
	}
	
	/**
	 * Synchronizes the Series information from the TvDb API to have the latest content.
	 */
	@Transactional
	public void synchSeries() {
		
		Settings lastUpdateSettings = settingsRepository.getSeriesLastUpdate();
		
		// TODO: Do initial load if it's the first time ever.
		// TODO: Improve the sync process to avoid updating episodes that haven't been modified.
		SeriesUpdateResponse response = tvDbApiService.getUpdatedSeries(lastUpdateSettings.getValue());
		
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
	}
	
	/**
	 * @param seriesId
	 */
	private void addSeries(Long seriesId) {
		Series series = tvDbApiService.getSeries(seriesId, peopleRepository);
		if (series != null) {
			watchableService.saveWatchable(series);
		}
		ExecutorUtils.sleep(SLEEP);
	}
}
