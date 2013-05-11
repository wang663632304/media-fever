package com.mediafever.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.push.PushService;
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
	private PushService pushService;
	
	@GET
	@Path("/mediaSessionInvitation")
	public void pushMediaSessionInvitation() {
		pushService.send(new MediaSessionInvitationGcmMessage("Maxi Rosson",
				"https://graph.facebook.com/maxirosson/picture?type=normal"), 1L);
	}
	
	@GET
	@Path("/friendRequest")
	public void pushFriendRequest() {
		pushService.send(new FriendRequestGcmMessage("Maxi Rosson",
				"https://graph.facebook.com/maxirosson/picture?type=normal"), 1L);
	}
	
	@GET
	@Path("/newEpisode")
	public void pushNewEpisode() {
		pushService.send(new NewEpisodeGcmMessage(1L, "Friends", "The One with the Monkey", 10,
				"https://graph.facebook.com/maxirosson/picture?type=normal"), 1L);
	}
}
