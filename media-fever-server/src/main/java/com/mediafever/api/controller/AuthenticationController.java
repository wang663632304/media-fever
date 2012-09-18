package com.mediafever.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.GZIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.controller.AbstractController;
import com.mediafever.api.controller.parser.LoginParser;
import com.mediafever.core.domain.User;
import com.mediafever.core.service.AuthenticationService;

/**
 * 
 * @author Maxi Rosson
 */
@Path(AuthenticationController.API_AUTH_PATH)
@Controller
public class AuthenticationController extends AbstractController {
	
	public static final String API_AUTH_PATH = "/api/auth";
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@GZIP
	public String login(String json) {
		LoginParser loginParser = new LoginParser();
		loginParser.parse(json);
		User user = authenticationService.loginUser(loginParser.getEmail(), loginParser.getPassword());
		return marshall(user);
	}
}
