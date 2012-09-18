package com.mediafever.core.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.repository.PeopleRepository;
import com.mediafever.core.repository.WatchableRepository;
import com.mediafever.core.service.moviedb.MovieDbApiService;
import com.mediafever.core.service.tvdb.TVDbApiService;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class SynchronizationService {
	
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
	
	public void synchSeries() {
		
		// TODO Change the id range
		for (long i = 0; i < 300; i++) {
			Series series = tvDbApiService.getSeries(i, peopleRepository);
			if (series != null) {
				watchableService.saveWatchable(series);
			}
			ExecutorUtils.sleep(SLEEP);
		}
		
		// TODO implement series update
	}
}
