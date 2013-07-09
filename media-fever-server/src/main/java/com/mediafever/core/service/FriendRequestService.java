package com.mediafever.core.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jdroid.javaweb.push.PushService;
import com.mediafever.core.domain.FriendRequest;
import com.mediafever.core.domain.User;
import com.mediafever.core.repository.FriendRequestRepository;
import com.mediafever.core.repository.UserRepository;
import com.mediafever.core.service.push.gcm.FriendRequestAcceptedGcmMessage;
import com.mediafever.core.service.push.gcm.FriendRequestGcmMessage;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class FriendRequestService {
	
	@Autowired
	private PushService pushService;
	
	@Autowired
	private FriendRequestRepository friendRequestRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	public FriendRequest createFriendRequest(Long senderId, Long friendId) {
		User sender = userRepository.get(senderId);
		User friend = userRepository.get(friendId);
		FriendRequest friendRequest = sender.inviteFriend(friend);
		if (friendRequest != null) {
			pushService.send(new FriendRequestGcmMessage(friendRequest.getSender().getFullName(),
					friendRequest.getSender().getImageUrl()), friendRequest.getUser().getDevices());
		}
		return friendRequest;
	}
	
	public List<FriendRequest> getFriendRequests(Long userId) {
		return friendRequestRepository.getByUserId(userId);
	}
	
	@Transactional
	public void acceptFriendRequest(Long friendRequestId) {
		FriendRequest friendRequest = friendRequestRepository.get(friendRequestId);
		friendRequest.accept();
		
		pushService.send(new FriendRequestAcceptedGcmMessage(), friendRequest.getSender().getDevices());
		
	}
	
	@Transactional
	public void rejectFriendRequest(Long friendRequestId) {
		FriendRequest friendRequest = friendRequestRepository.get(friendRequestId);
		friendRequest.reject();
	}
}
