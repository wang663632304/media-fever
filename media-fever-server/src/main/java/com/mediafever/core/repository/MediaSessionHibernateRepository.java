package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.mediafever.core.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionHibernateRepository extends HibernateRepository<MediaSession> implements
		MediaSessionRepository {
	
	protected MediaSessionHibernateRepository() {
		super(MediaSession.class);
	}
	
	/**
	 * @see com.mediafever.core.repository.MediaSessionRepository#getAll(java.lang.Long)
	 */
	@Override
	public List<MediaSession> getAll(Long userId) {
		DetachedCriteria criteria = createDetachedCriteria();
		DetachedCriteria usersCriteria = criteria.createCriteria("users");
		usersCriteria.createCriteria("user").add(Restrictions.eq("id", userId));
		usersCriteria.add(Restrictions.or(Restrictions.isNull("accepted"), Restrictions.eq("accepted", true)));
		return find(criteria);
	}
}
