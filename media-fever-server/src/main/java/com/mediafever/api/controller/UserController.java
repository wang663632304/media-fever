package com.mediafever.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.java.utils.CollectionUtils;
import com.jdroid.java.utils.FileUtils;
import com.jdroid.javaweb.controller.AbstractController;
import com.jdroid.javaweb.domain.FileEntity;
import com.jdroid.javaweb.search.Filter;
import com.mediafever.api.controller.parser.FacebookAccountParser;
import com.mediafever.api.controller.parser.FacebookAccountParser.FacebookAccountJson;
import com.mediafever.api.controller.parser.UserParser;
import com.mediafever.api.controller.parser.UserParser.UserJson;
import com.mediafever.core.domain.User;
import com.mediafever.core.domain.watchable.WatchableType;
import com.mediafever.core.repository.CustomFilterKey;
import com.mediafever.core.service.UserService;
import com.mediafever.core.service.WatchableService;

/**
 * 
 * @author Maxi Rosson
 */
@Path(UserController.API_USERS_PATH)
@Controller
public class UserController extends AbstractController {
	
	public static final String API_USERS_PATH = "/api/users";
	private static final String PROFILE = "profile";
	private static final String IMAGE = "image";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WatchableService watchableService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@GZIP
	public String signup(String userJSON) {
		UserJson userJson = (UserJson)(new UserParser().parse(userJSON));
		User user = userService.addUser(userJson.getEmail(), userJson.getPassword(), userJson.getFirstName(),
			userJson.getLastName(), userJson.getPublicProfile());
		return marshall(user);
	}
	
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("multipart/form-data")
	@GZIP
	@SuppressWarnings("resource")
	public String editProfile(@PathParam("id") Long id, MultipartFormDataInput input) throws IOException {
		
		FileEntity image = null;
		String userJSON = input.getFormDataMap().get(PROFILE).get(0).getBodyAsString();
		UserJson userJson = (UserJson)(new UserParser().parse(userJSON));
		
		List<InputPart> parts = input.getFormDataMap().get(IMAGE);
		if (CollectionUtils.isNotEmpty(parts)) {
			InputStream inputStream = parts.get(0).getBody(InputStream.class, null);
			byte[] bytes = FileUtils.readAsBytes(inputStream);
			image = new FileEntity(bytes, id + "-" + System.currentTimeMillis() + ".png");
		}
		
		input.close();
		
		User user = userService.updateUser(id, userJson.getEmail(), userJson.getPassword(), userJson.getFirstName(),
			userJson.getLastName(), userJson.getPublicProfile(), image);
		return marshall(user);
	}
	
	@POST
	@Path("{id}/facebook")
	@Consumes(MediaType.APPLICATION_JSON)
	@GZIP
	public void linkFacebookAccount(@PathParam("id") Long userId, String facebookAccountJSON) {
		FacebookAccountJson facebookAccountJson = (FacebookAccountJson)(new FacebookAccountParser().parse(facebookAccountJSON));
		userService.linkToFacebookAccount(userId, facebookAccountJson.getUserId(),
			facebookAccountJson.getAccessToken(), facebookAccountJson.getAccessExpiresIn());
	}
	
	@GET
	@Path("{id}/facebook")
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getFacebookAccount(@PathParam("id") Long userId) {
		return marshallSimple(userService.getFacebookAccount(userId));
	}
	
	@DELETE
	@Path("{id}/facebook")
	public void unlinkFacebookAccount(@PathParam("id") Long userId) {
		userService.unlinkFacebookAccount(userId);
	}
	
	@GET
	@Path("{id}/friends")
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getFriends(@PathParam("id") Long id) {
		return marshallSimple(userService.getFriends(id));
	}
	
	@DELETE
	@Path("{id}/friends/{friendId}")
	@GZIP
	public void removeFriend(@PathParam("id") Long id, @PathParam("friendId") Long friendId) {
		userService.removeFriend(id, friendId);
	}
	
	@GET
	@Path("{id}/watchables/suggestions")
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String getSuggestions(@PathParam("id") Long id, @QueryParam("page") Integer page,
			@QueryParam("pageSize") Integer pageSize, @QueryParam("watchableType") String watchableType) {
		
		// TODO Use the SMART Selection algorithm here
		Filter filter = new Filter(page, pageSize);
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, WatchableType.findByNames(watchableType));
		return marshallSimple(watchableService.searchWatchable(filter));
	}
}
