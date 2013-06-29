package com.mediafever.core.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.javaweb.exception.InvalidAuthenticationException;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceRepository;
import com.jdroid.javaweb.push.DeviceType;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.User;
import com.mediafever.core.repository.UserRepository;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	/**
	 * @param email The user's email
	 * @param password The user's password
	 * @param installationId
	 * @param deviceType
	 * @return The User
	 * @throws InvalidAuthenticationException Thrown if the email and/or the password are invalid
	 */
	@Transactional
	public User loginUser(String email, String password, String installationId, DeviceType deviceType)
			throws InvalidAuthenticationException {
		User user = ApplicationContext.get().getSecurityContext().authenticateUser(email, password);
		
		Device device = deviceRepository.find(installationId, deviceType);
		if (device == null) {
			device = new Device(installationId, deviceType);
			deviceRepository.add(device);
		} else {
			detachDeviceFromUsers(device);
		}
		user.addDevice(device);
		return user;
	}
	
	@Transactional
	public void logoutUser(Long userId, String installationId, DeviceType deviceType) {
		
		Device device = deviceRepository.find(installationId, deviceType);
		if (userId != null) {
			User user = userRepository.get(userId);
			user.removeDevice(device);
		} else {
			detachDeviceFromUsers(device);
		}
	}
	
	private void detachDeviceFromUsers(Device device) {
		List<User> users = userRepository.getByDevice(device);
		for (User each : users) {
			each.removeDevice(device);
		}
	}
}
