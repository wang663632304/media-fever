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
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.annotations.GZIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.javaweb.controller.AbstractController;
import com.mediafever.api.controller.parser.FriendRequestParser;
import com.mediafever.api.controller.parser.FriendRequestParser.FriendRequestJson;
import com.mediafever.core.domain.FriendRequest;
import com.mediafever.core.service.FriendRequestService;

/**
 * 
 * @author Maxi Rosson
 */
@Path("api/friendRequests")
@Controller
public class FriendRequestController extends AbstractController {
	
	@Autowired
	private FriendRequestService friendRequestService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getFriendRequests(@QueryParam("userId") Long userId) {
		return marshallSimple(friendRequestService.getFriendRequests(userId));
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@GZIP
	public String createFriendRequest(String friendRequestJSON) {
		FriendRequestJson friendRequestJson = (FriendRequestJson)(new FriendRequestParser().parse(friendRequestJSON));
		FriendRequest friendRequest = friendRequestService.createFriendRequest(friendRequestJson.getSenderId(),
			friendRequestJson.getUserId());
		return friendRequest != null ? marshallSimple(friendRequest) : StringUtils.EMPTY;
	}
	
	@PUT
	@Path("{id}/accept")
	@GZIP
	public void acceptFriendRequest(@PathParam("id") Long id) {
		friendRequestService.acceptFriendRequest(id);
	}
	
	@PUT
	@Path("{id}/reject")
	@GZIP
	public void rejectFriendRequest(@PathParam("id") Long id) {
		friendRequestService.rejectFriendRequest(id);
	}
}
