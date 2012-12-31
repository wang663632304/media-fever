package com.mediafever.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.mediafever.api.controller.parser.MediaSessionParser;
import com.mediafever.api.controller.parser.MediaSessionParser.MediaSessionJson;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.session.MediaSession;
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
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String createMediaSession(String mediaSessionJSON) {
		MediaSessionJson mediaSessionJson = (MediaSessionJson)(new MediaSessionParser().parse(mediaSessionJSON));
		MediaSession mediaSession = mediaSessionService.createMediaSession(mediaSessionJson.getDate(),
			mediaSessionJson.getTime(), mediaSessionJson.getWatchableTypes(), mediaSessionJson.getUsersIds());
		return marshall(mediaSession);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getAll(@QueryParam("userId") Long userId) {
		return marshallSimple(mediaSessionService.getAll(userId));
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String get(@PathParam("id") Long id) {
		return marshall(mediaSessionService.get(id));
	}
	
	@PUT
	@Path("{id}/accept")
	@GZIP
	public void acceptMediaSession(@PathParam("id") Long id) {
		mediaSessionService.acceptMediaSession(id, ApplicationContext.get().getSecurityContext().getUser().getId());
	}
	
	@PUT
	@Path("{id}/reject")
	@GZIP
	public void rejectMediaSession(@PathParam("id") Long id) {
		mediaSessionService.rejectMediaSession(id, ApplicationContext.get().getSecurityContext().getUser().getId());
	}
	
	@GET
	@Path("{id}/selection/smart")
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getSmartSelection(@PathParam("id") Long id) {
		return marshallSimple(mediaSessionService.getSmartSelection(id));
	}
}
