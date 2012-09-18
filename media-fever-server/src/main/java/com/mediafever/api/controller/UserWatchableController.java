package com.mediafever.api.controller;

import java.util.Map;
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
import com.google.common.collect.Maps;
import com.jdroid.javaweb.controller.AbstractController;
import com.jdroid.javaweb.search.Filter;
import com.mediafever.api.controller.parser.MarkAsWatchedParser;
import com.mediafever.api.controller.parser.UserWatchableParser;
import com.mediafever.api.controller.parser.MarkAsWatchedParser.MarkAsWatchedJson;
import com.mediafever.api.controller.parser.UserWatchableParser.UserWatchableJson;
import com.mediafever.api.marshaller.json.SeasonJsonMarshaller;
import com.mediafever.core.domain.UserWatchable;
import com.mediafever.core.domain.watchable.WatchableType;
import com.mediafever.core.repository.CustomFilterKey;
import com.mediafever.core.service.UserWatchableService;

/**
 * 
 * @author Maxi Rosson
 */
@Path("api/userWatchables")
@Controller
public class UserWatchableController extends AbstractController {
	
	@Autowired
	private UserWatchableService userWatchableService;
	
	@GET
	@Path("search")
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String searchUserWatchables(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
			@QueryParam("userId") Long userId, @QueryParam("watched") Boolean watched,
			@QueryParam("isInWishList") Boolean isInWishList, @QueryParam("watchableType") String watchableType) {
		Filter filter = new Filter(page, pageSize);
		filter.addValue(CustomFilterKey.USER_ID, userId);
		filter.addValue(CustomFilterKey.WATCHED, watched);
		filter.addValue(CustomFilterKey.IS_IN_WISHLIST, isInWishList);
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, WatchableType.findByNames(watchableType));
		return marshallSimple(userWatchableService.searchUserWatchables(filter));
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String searchUserWatchable(@QueryParam("userId") Long userId, @QueryParam("watchableId") Long watchableId) {
		UserWatchable userWatchable = userWatchableService.searchUserWatchable(userId, watchableId);
		Map<String, String> extras = Maps.newHashMap();
		extras.put(SeasonJsonMarshaller.USER_ID_EXTRA, userId.toString());
		return marshall(userWatchable, extras);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@GZIP
	public String createUserWatchable(String userWatchableJSON) {
		UserWatchableJson userWatchableJson = (UserWatchableJson)(new UserWatchableParser().parse(userWatchableJSON));
		UserWatchable userWatchable = userWatchableService.createUserWatchable(userWatchableJson.getUserId(),
			userWatchableJson.getWatchableId(), userWatchableJson.isWatched(), userWatchableJson.isInWishList());
		Map<String, String> extras = Maps.newHashMap();
		extras.put(SeasonJsonMarshaller.USER_ID_EXTRA, userWatchableJson.getUserId().toString());
		return marshall(userWatchable, extras);
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@GZIP
	public void updateUserWatchable(@PathParam("id") Long id, String userWatchableJSON) {
		UserWatchableJson userWatchableJson = (UserWatchableJson)(new UserWatchableParser().parse(userWatchableJSON));
		userWatchableService.updateUserWatchable(id, userWatchableJson.isWatched(), userWatchableJson.isInWishList());
	}
	
	@PUT
	@Path("markAsWatched")
	@Consumes(MediaType.APPLICATION_JSON)
	@GZIP
	public void markAsWatched(String markAsWatchedJSON) {
		MarkAsWatchedJson markAsWatchedJson = (MarkAsWatchedJson)(new MarkAsWatchedParser().parse(markAsWatchedJSON));
		userWatchableService.markAsWatched(markAsWatchedJson.getUserId(), markAsWatchedJson.getWatchablesIds(),
			markAsWatchedJson.getWatched());
	}
}
