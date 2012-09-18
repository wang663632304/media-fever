package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.mediafever.core.domain.FriendRequest;

/**
 * 
 * @author Maxi Rosson
 */
public class FriendRequestHibernateRepository extends HibernateRepository<FriendRequest> implements
		FriendRequestRepository {
	
	protected FriendRequestHibernateRepository() {
		super(FriendRequest.class);
	}
	
	/**
	 * @see com.mediafever.core.repository.FriendRequestRepository#getByUserId(java.lang.Long)
	 */
	@Override
	public List<FriendRequest> getByUserId(Long userId) {
		DetachedCriteria criteria = createDetachedCriteria();
		criteria.createCriteria("user").add(Restrictions.eq("id", userId));
		return find(criteria);
	}
}
