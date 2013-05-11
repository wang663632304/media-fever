package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.jdroid.java.collections.Lists;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.UserWatchable;
import com.mediafever.core.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class UserWatchableHibernateRepository extends HibernateRepository<UserWatchable> implements
		UserWatchableRepository {
	
	protected UserWatchableHibernateRepository() {
		super(UserWatchable.class);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserWatchableRepository#search(com.jdroid.javaweb.search.Filter)
	 */
	@Override
	public PagedResult<UserWatchable> search(Filter filter) {
		DetachedCriteria criteria = this.createDetachedCriteria();
		addUserFilter(criteria, filter);
		addWishListFilter(criteria, filter);
		addWatchedFilter(criteria, filter);
		addWatchableTypesFilter(criteria.createCriteria("watchable"), filter);
		return this.find(criteria, filter.getPager(), filter.getSorting());
	}
	
	/**
	 * @see com.mediafever.core.repository.UserWatchableRepository#get(java.lang.Long, java.lang.Long)
	 */
	@Override
	public UserWatchable get(Long userId, Long watchableId) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.createCriteria("watchable").add(Restrictions.eq("id", watchableId));
		criteria.createCriteria("user").add(Restrictions.eq("id", userId));
		return findUnique(criteria);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserWatchableRepository#findAll(java.lang.Long, java.util.List)
	 */
	@Override
	public List<UserWatchable> findAll(Long userId, List<Long> watchablesIds) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.createCriteria("watchable").add(Restrictions.in("id", watchablesIds));
		criteria.createCriteria("user").add(Restrictions.eq("id", userId));
		return find(criteria);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserWatchableRepository#getWatchedBy(java.lang.Long,
	 *      com.mediafever.core.domain.watchable.WatchableType)
	 */
	@Override
	public List<UserWatchable> getWatchedBy(Long watchableExternalId, WatchableType watchableType) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("watched", true));
		DetachedCriteria watchableCriteria = criteria.createCriteria("watchable").add(
			Restrictions.eq("externalId", watchableExternalId));
		addWatchableTypeRestriction(watchableCriteria, Lists.newArrayList(watchableType));
		return find(criteria);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserWatchableRepository#getWatchedBy(java.util.List, java.lang.Long)
	 */
	@Override
	public List<UserWatchable> getWatchedBy(List<Long> userIds, Long watchableId) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("watched", true));
		criteria.createCriteria("user").add(Restrictions.in("id", userIds));
		criteria.createCriteria("watchable").add(Restrictions.eq("id", watchableId));
		return find(criteria);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserWatchableRepository#getOnTheWishListOf(java.util.List, java.lang.Long)
	 */
	@Override
	public List<UserWatchable> getOnTheWishListOf(List<Long> userIds, Long watchableId) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.add(Restrictions.eq("isInWishList", true));
		criteria.createCriteria("user").add(Restrictions.in("id", userIds));
		criteria.createCriteria("watchable").add(Restrictions.eq("id", watchableId));
		return find(criteria);
	}
}
