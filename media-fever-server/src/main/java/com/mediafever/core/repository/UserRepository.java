package com.mediafever.core.repository;

import java.util.List;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.repository.Repository;
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
	 * @param input
	 * @return The {@link User}s
	 */
	public List<User> autocomplete(String input);
	
}
