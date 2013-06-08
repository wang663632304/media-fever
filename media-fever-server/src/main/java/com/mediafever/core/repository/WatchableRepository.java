package com.mediafever.core.repository;

import java.util.Date;
import java.util.List;
import com.jdroid.java.repository.Repository;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.watchable.Episode;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.domain.watchable.WatchableType;

/**
 * Repository that handles the persistence of {@link Watchable}s.
 * 
 * @author Maxi Rosson
 */
public interface WatchableRepository extends Repository<Watchable> {
	
	/**
	 * Searches for {@link Watchable}s that comply with the given {@link Filter}.
	 * 
	 * @param filter The {@link Filter}.
	 * @return A list of {@link Watchable}s
	 */
	public PagedResult<Watchable> search(Filter filter);
	
	/**
	 * Gets a {@link Watchable} by its external id and type.
	 * 
	 * @param externalId The {@link Watchable}'s external id.
	 * @param watchableType The {@link Watchable}'s type.
	 * @return The {@link Watchable}.
	 */
	public Watchable getByExternalId(Long externalId, WatchableType watchableType);
	
	public Long getLastMovieExternalId();
	
	public PagedResult<Watchable> getMovies(Integer page);
	
	/**
	 * Gets all the {@link Series} that have at least an {@link Episode} that is release on the given {@link Date}.
	 * 
	 * @param date The {@link Date} the episode is first aired.
	 * @return The list of {@link Series}.
	 */
	public List<Series> getSeriesWithReleasedEpisodes(Date date);
}
