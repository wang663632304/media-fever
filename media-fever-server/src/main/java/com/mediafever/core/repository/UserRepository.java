package com.mediafever.core.repository;

import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.repository.Repository;
import com.jdroid.javaweb.search.Filter;
import com.jdroid.javaweb.search.PagedResult;
import com.mediafever.core.domain.User;

/**
 * 
 * @author Maxi Rosson
 */
public interface UserRepository extends Repository<User> {
	
	/**
	 * @param email The email of the searched {@link User}
	 * @return the {@link User}
	 * @throws ObjectNotFoundException If doesn't exist an {@link User} with the received email
	 */
	public User getByEmail(String email) throws ObjectNotFoundException;
	
	/**
	 * @param userToken The userToken of the searched {@link User}
	 * @return the {@link User}
	 * @throws ObjectNotFoundException If doesn't exist an {@link User} with the received userToken
	 */
	public User getByUserToken(String userToken) throws ObjectNotFoundException;
	
	/**
	 * @param email The email to search for
	 * @return Whether an user with the given email exists or not
	 */
	public Boolean existsWithEmail(String email);
	
	/**
	 * @param filter
	 * @return The {@link User}s
	 */
	public PagedResult<User> search(Filter filter);
	
}
