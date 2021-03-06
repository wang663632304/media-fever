package com.mediafever.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.jdroid.javaweb.search.Sorting;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.domain.watchable.visitor.DummyWatchableVisitor;
import com.mediafever.core.domain.watchable.visitor.WatchableVisitor;
import com.mediafever.core.repository.CustomSortingKey;
import com.mediafever.core.repository.WatchableRepository;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class WatchableService {
	
	@Autowired
	private WatchableRepository watchableRepository;
	
	@Transactional
	public void addWatchable(Watchable watchable) {
		watchableRepository.add(watchable);
	}
	
	/**
	 * Saves the given {@link Watchable} in the database. If it already exists, it is updated.
	 * 
	 * @param watchable The {@link Watchable} to save.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveWatchable(Watchable watchable) {
		saveWatchable(watchable, new DummyWatchableVisitor());
	}
	
	/**
	 * Saves the given {@link Watchable} in the database. If it already exists, it is updated.
	 * 
	 * @param watchable The {@link Watchable} to save.
	 * @param watchableVisitor A {@link WatchableVisitor} to use in the save process.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveWatchable(Watchable watchable, WatchableVisitor watchableVisitor) {
		try {
			Watchable existentWatchable = watchableRepository.getByExternalId(watchable.getExternalId(),
				watchable.getType());
			existentWatchable.updateFrom(watchable, watchableVisitor);
		} catch (ObjectNotFoundException e) {
			addWatchable(watchable);
		}
	}
	
	public PagedResult<Watchable> searchWatchable(Filter filter) {
		filter.setSorting(new Sorting(CustomSortingKey.RELEASE_DATE, false));
		return watchableRepository.search(filter);
	}
	
	public Watchable getWatchable(Long watchableId) {
		return watchableRepository.get(watchableId);
	}
}
