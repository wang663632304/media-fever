package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.core.domain.User;

public class UserHibernateRepository extends HibernateRepository<User> implements UserRepository {
	
	protected UserHibernateRepository() {
		super(User.class);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserRepository#getByEmail(java.lang.String)
	 */
	@Override
	public User getByEmail(String email) throws ObjectNotFoundException {
		return findUnique("email", email);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserRepository#getByUserToken(java.lang.String)
	 */
	@Override
	public User getByUserToken(String userToken) throws ObjectNotFoundException {
		return findUnique("userToken", userToken);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserRepository#existsWithEmail(java.lang.String)
	 */
	@Override
	public Boolean existsWithEmail(String email) {
		return exists("email", email);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserRepository#autocomplete(java.lang.String)
	 */
	@Override
	public List<User> autocomplete(String input) {
		DetachedCriteria usersCriteria = createDetachedCriteria();
		for (String word : StringUtils.splitToCollection(input, StringUtils.SPACE)) {
			usersCriteria.add(Restrictions.or(Restrictions.like("firstName", word, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("lastName", word, MatchMode.ANYWHERE).ignoreCase()));
		}
		return find(usersCriteria);
	}
}
