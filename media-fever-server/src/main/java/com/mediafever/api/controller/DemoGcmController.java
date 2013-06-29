package com.mediafever.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.push.PushService;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.User;
import com.mediafever.core.repository.UserRepository;
import com.mediafever.core.service.push.gcm.FriendRequestGcmMessage;
import com.mediafever.core.service.push.gcm.MediaSessionInvitationGcmMessage;
import com.mediafever.core.service.push.gcm.NewEpisodeGcmMessage;

/**
 * @author Maxi Rosson
 */
@Path("gcm")
@Controller
public class DemoGcmController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PushService pushService;
	
	@GET
	@Path("/mediaSessionInvitation")
	public void pushMediaSessionInvitation() {
		User senderUser = ApplicationContext.get().getSecurityContext().authenticateUser("userToken2");
		User receiverUser = userRepository.get(1L);
		pushService.send(new MediaSessionInvitationGcmMessage(senderUser.getFullName(), senderUser.getImageUrl()),
			receiverUser.getDevices());
	}
	
	@GET
	@Path("/friendRequest")
	public void pushFriendRequest() {
		User senderUser = ApplicationContext.get().getSecurityContext().authenticateUser("userToken2");
		User receiverUser = userRepository.get(1L);
		pushService.send(new FriendRequestGcmMessage(senderUser.getFullName(), senderUser.getImageUrl()),
			receiverUser.getDevices());
	}
	
	@GET
	@Path("/newEpisode")
	public void pushNewEpisode() {
		User receiverUser = userRepository.get(1L);
		pushService.send(new NewEpisodeGcmMessage(1L, "Friends", "The One with the Monkey", 10,
				"https://graph.facebook.com/maxirosson/picture?type=normal"), receiverUser.getDevices());
	}
}
