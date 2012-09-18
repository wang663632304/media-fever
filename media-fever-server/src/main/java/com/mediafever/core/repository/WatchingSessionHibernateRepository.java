package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.mediafever.core.domain.watchingsession.WatchingSession;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionHibernateRepository extends HibernateRepository<WatchingSession> implements
		WatchingSessionRepository {
	
	protected WatchingSessionHibernateRepository() {
		super(WatchingSession.class);
	}
	
	/**
	 * @see com.mediafever.core.repository.WatchingSessionRepository#getAll(java.lang.Long)
	 */
	@Override
	public List<WatchingSession> getAll(Long userId) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.createCriteria("users").createCriteria("user").add(Restrictions.eq("id", userId));
		criteria.addOrder(Order.asc("date"));
		return find(criteria);
	}
}
