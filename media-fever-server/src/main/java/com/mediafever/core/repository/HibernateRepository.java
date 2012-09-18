package com.mediafever.core.repository;

import java.util.Collection;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import com.google.common.collect.Lists;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.domain.Entity;
import com.jdroid.javaweb.hibernate.AbstractHibernateRepository;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.utils.CollectionUtils;
import com.mediafever.core.domain.watchable.WatchableType;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public abstract class HibernateRepository<T extends Entity> extends AbstractHibernateRepository<T> {
	
	public HibernateRepository(Class<T> entityClass) {
		super(entityClass);
	}
	
	protected void addNameFilter(DetachedCriteria watchableCriteria, Filter filter) {
		String name = filter.getStringValue(CustomFilterKey.NAME);
		if (StringUtils.isNotBlank(name)) {
			watchableCriteria.add(Restrictions.ilike("name", name, MatchMode.ANYWHERE));
		}
	}
	
	protected void addImageFilter(DetachedCriteria watchableCriteria, Filter filter) {
		Boolean imageRequired = filter.getBooleanValue(CustomFilterKey.IMAGE_REQUIRED);
		if ((imageRequired != null) && imageRequired) {
			watchableCriteria.add(Restrictions.isNotNull("imageURL"));
		}
	}
	
	protected void addRatingFilter(DetachedCriteria watchableCriteria, Filter filter) {
		Integer minimumRating = filter.getIntegerValue(CustomFilterKey.RATING_GREATER_THAN);
		if (minimumRating != null) {
			watchableCriteria.add(Restrictions.or(Restrictions.gt("rating", minimumRating.floatValue()),
				Restrictions.eq("rating", 0f)));
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void addWatchableTypesFilter(DetachedCriteria watchableCriteria, Filter filter) {
		
		Collection<WatchableType> watchableTypes = filter.getCollectionValue(CustomFilterKey.WATCHABLE_TYPES);
		if (com.jdroid.java.utils.CollectionUtils.isNotEmpty(watchableTypes)) {
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("{alias}.dtype IN (");
			sqlBuilder.append(CollectionUtils.join(watchableTypes, AbstractHibernateRepository.SQL_REPLACEMENT));
			sqlBuilder.append(")");
			
			List<Object> values = Lists.newArrayList();
			List<Type> valueTypes = Lists.newArrayList();
			for (WatchableType each : watchableTypes) {
				values.add(each.getWatchableSimpleClassName());
				valueTypes.add(Hibernate.STRING);
			}
			watchableCriteria.add(Restrictions.sqlRestriction(sqlBuilder.toString(), values.toArray(),
				valueTypes.toArray(new Type[] {})));
		}
	}
	
	protected void addWatchedFilter(DetachedCriteria userWatchableCriteria, Filter filter) {
		Boolean watched = filter.getBooleanValue(CustomFilterKey.WATCHED);
		if (watched != null) {
			userWatchableCriteria.add(Restrictions.eq("watched", watched));
		}
	}
	
	protected void addWishListFilter(DetachedCriteria userWatchableCriteria, Filter filter) {
		Boolean isInWishList = filter.getBooleanValue(CustomFilterKey.IS_IN_WISHLIST);
		if (isInWishList != null) {
			userWatchableCriteria.add(Restrictions.eq("isInWishList", isInWishList));
		}
	}
	
	protected void addUserFilter(DetachedCriteria userWatchableCriteria, Filter filter) {
		Long userId = filter.getLongValue(CustomFilterKey.USER_ID);
		if (userId != null) {
			userWatchableCriteria.createCriteria("user").add(Restrictions.eq("id", userId));
		}
	}
	
}
