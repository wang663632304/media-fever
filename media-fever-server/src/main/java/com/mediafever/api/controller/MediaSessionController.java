package com.mediafever.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.GZIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.controller.AbstractController;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.service.MediaSessionService;

/**
 * 
 * @author Maxi Rosson
 */
@Path("api/mediaSessions")
@Controller
public class MediaSessionController extends AbstractController {
	
	@Autowired
	private MediaSessionService mediaSessionService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getAll(@QueryParam("userId") Long userId) {
		return marshallSimple(mediaSessionService.getAll(userId));
	}
	
	@PUT
	@Path("{id}/accept")
	@GZIP
	public void acceptFriendRequest(@PathParam("id") Long id) {
		mediaSessionService.acceptMediaSession(id, ApplicationContext.get().getSecurityContext().getUser().getId());
	}
	
	@PUT
	@Path("{id}/reject")
	@GZIP
	public void rejectFriendRequest(@PathParam("id") Long id) {
		mediaSessionService.rejectMediaSession(id, ApplicationContext.get().getSecurityContext().getUser().getId());
	}
}
