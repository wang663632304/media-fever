package com.mediafever.core.repository;

import com.jdroid.java.repository.Repository;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.watchable.Watchable;

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
	 * Gets a {@link Watchable} by its external id.
	 * 
	 * @param externalId The {@link Watchable}'s external id.
	 * @return The {@link Watchable}.
	 */
	public Watchable getByExternalId(Long externalId);
	
	public Long getLastMovieExternalId();
	
	public PagedResult<Watchable> getMovies(Integer page);
}
