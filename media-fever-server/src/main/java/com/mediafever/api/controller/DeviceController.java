package com.mediafever.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushService;
import com.mediafever.api.controller.parser.DeviceParser;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.User;

/**
 * @author Maxi Rosson
 */
@Path("api/devices")
@Controller
public class DeviceController {
	
	@Autowired
	private PushService pushService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void enableDevice(@HeaderParam("User-Agent") String userAgent, String json) {
		DeviceParser deviceParser = new DeviceParser();
		deviceParser.parse(json);
		User user = ApplicationContext.get().getSecurityContext().getUser();
		pushService.enableDevice(user.getId(), deviceParser.getInstallationId(), deviceParser.getRegistrationId(),
			DeviceType.find(userAgent));
	}
	
	@DELETE
	@Path("{installationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void disableDevice(@PathParam("installationId") String installationId) {
		User user = ApplicationContext.get().getSecurityContext().getUser();
		pushService.disableDevice(user != null ? user.getId() : null, installationId);
	}
}
