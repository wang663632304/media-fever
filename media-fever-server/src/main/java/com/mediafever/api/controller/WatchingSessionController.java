package com.mediafever.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.GZIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.controller.AbstractController;
import com.mediafever.core.service.WatchingSessionService;

/**
 * 
 * @author Maxi Rosson
 */
@Path("api/watchingSessions")
@Controller
public class WatchingSessionController extends AbstractController {
	
	@Autowired
	private WatchingSessionService watchingSessionService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getAll(@QueryParam("userId") Long userId) {
		return marshallSimple(watchingSessionService.getAll(userId));
	}
}
