package com.mediafever.core.repository;

import com.jdroid.java.repository.ObjectNotFoundException;
import com.mediafever.core.domain.watchable.Person;

/**
 * 
 * @author Maxi Rosson
 */
public class PeopleHibernateRepository extends HibernateRepository<Person> implements PeopleRepository {
	
	protected PeopleHibernateRepository() {
		super(Person.class);
	}
	
	/**
	 * @see com.mediafever.core.repository.PeopleRepository#getByFullname(java.lang.String)
	 */
	@Override
	public Person getByFullname(String fullname) throws ObjectNotFoundException {
		return findUnique("fullname", fullname);
	}
}
