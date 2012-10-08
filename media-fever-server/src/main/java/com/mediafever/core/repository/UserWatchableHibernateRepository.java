package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.UserWatchable;

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
}