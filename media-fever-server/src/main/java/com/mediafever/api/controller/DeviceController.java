package com.mediafever.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushService;
import com.mediafever.api.controller.parser.DeviceParser;

/**
 * @author Maxi Rosson
 */
@Path(DeviceController.API_DEVICES_PATH)
@Controller
public class DeviceController {
	
	public static final String API_DEVICES_PATH = "/api/devices";
	
	@Autowired
	private PushService pushService;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void enableDevice(@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("installationId") String installationId, String json) {
		DeviceParser deviceParser = new DeviceParser();
		deviceParser.parse(json);
		pushService.enableDevice(installationId, DeviceType.find(userAgent), deviceParser.getRegistrationId());
	}
	
	@DELETE
	@Path("{installationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void disableDevice(@HeaderParam("User-Agent") String userAgent,
			@HeaderParam("installationId") String installationId) {
		pushService.disableDevice(installationId, DeviceType.find(userAgent));
	}
}
