package com.mediafever.core.repository;

import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.repository.Repository;
import com.mediafever.core.domain.watchable.Person;

/**
 * 
 * @author Maxi Rosson
 */
public interface PeopleRepository extends Repository<Person> {
	
	public Person getByFullname(String fullname) throws ObjectNotFoundException;
	
}
