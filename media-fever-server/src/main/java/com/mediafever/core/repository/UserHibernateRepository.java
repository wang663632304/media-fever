package com.mediafever.core.repository;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
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
	 * @see com.mediafever.core.repository.UserRepository#getByDevice(com.jdroid.javaweb.push.Device)
	 */
	@Override
	public List<User> getByDevice(Device device) {
		DetachedCriteria usersCriteria = createDetachedCriteria();
		usersCriteria.createAlias("devices", "device");
		usersCriteria.add(Restrictions.eq("device.id", device.getId()));
		return find(usersCriteria);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserRepository#existsWithEmail(java.lang.String)
	 */
	@Override
	public Boolean existsWithEmail(String email) {
		return exists("email", email);
	}
	
	/**
	 * @see com.mediafever.core.repository.UserRepository#search(com.jdroid.javaweb.search.Filter)
	 */
	@Override
	public PagedResult<User> search(Filter filter) {
		DetachedCriteria usersCriteria = createDetachedCriteria();
		
		String query = filter.getStringValue(CustomFilterKey.USER_QUERY);
		if (StringUtils.isNotBlank(query)) {
			for (String word : StringUtils.splitToCollection(query, StringUtils.SPACE)) {
				usersCriteria.add(Restrictions.or(Restrictions.or(
					Restrictions.like("firstName", word, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("lastName", word, MatchMode.ANYWHERE).ignoreCase()),
					Restrictions.like("email", word, MatchMode.ANYWHERE).ignoreCase()));
			}
		}
		usersCriteria.add(Restrictions.eq("publicProfile", true));
		return this.find(usersCriteria, filter.getPager(), filter.getSorting());
	}
}
