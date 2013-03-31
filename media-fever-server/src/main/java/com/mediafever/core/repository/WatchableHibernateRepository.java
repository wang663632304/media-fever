package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.jdroid.java.collections.Lists;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableHibernateRepository extends HibernateRepository<Watchable> implements WatchableRepository {
	
	protected WatchableHibernateRepository() {
		super(Watchable.class);
	}
	
	/**
	 * @see com.mediafever.core.repository.WatchableRepository#search(com.jdroid.javaweb.search.Filter)
	 */
	@Override
	public PagedResult<Watchable> search(Filter filter) {
		DetachedCriteria criteria = this.createDetachedCriteria();
		addNameFilter(criteria, filter);
		addWatchableTypesFilter(criteria, filter);
		addImageFilter(criteria, filter);
		addRatingFilter(criteria, filter);
		return this.find(criteria, filter.getPager(), filter.getSorting());
	}
	
	/**
	 * @see com.mediafever.core.repository.WatchableRepository#getByExternalId(Long, WatchableType)
	 */
	@Override
	public Watchable getByExternalId(Long externalId, WatchableType watchableType) {
		DetachedCriteria criteria = this.createDetachedCriteria();
		criteria.add(Restrictions.eq("externalId", externalId));
		addWatchableTypeRestriction(criteria, Lists.newArrayList(watchableType));
		return findUnique(criteria);
		
	}
	
	/**
	 * @see com.mediafever.core.repository.WatchableRepository#getLastMovieExternalId()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getLastMovieExternalId() {
		DetachedCriteria criteria = this.createDetachedCriteria();
		Filter filter = new Filter();
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, Lists.newArrayList(WatchableType.MOVIE));
		addWatchableTypesFilter(criteria, filter);
		criteria.setProjection(Projections.max("externalId"));
		Long lastMovieExternalId = null;
		List<Long> list = this.getHibernateTemplate().findByCriteria(criteria);
		if (!list.isEmpty()) {
			lastMovieExternalId = list.iterator().next();
		}
		return lastMovieExternalId != null ? lastMovieExternalId : 0L;
	}
	
	/**
	 * @see com.mediafever.core.repository.WatchableRepository#getMovies(java.lang.Integer)
	 */
	@Override
	public PagedResult<Watchable> getMovies(Integer page) {
		DetachedCriteria criteria = this.createDetachedCriteria();
		Filter filter = new Filter(page, 50);
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, Lists.newArrayList(WatchableType.MOVIE));
		addWatchableTypesFilter(criteria, filter);
		return this.find(criteria, filter.getPager(), filter.getSorting());
	}
}
