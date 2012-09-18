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
import com.jdroid.javaweb.search.Filter;
import com.mediafever.core.domain.watchable.WatchableType;
import com.mediafever.core.repository.CustomFilterKey;
import com.mediafever.core.service.WatchableService;

/**
 * 
 * @author Maxi Rosson
 */
@Path("api/watchables")
@Controller
public class WatchableController extends AbstractController {
	
	@Autowired
	private WatchableService watchableService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String search(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize,
			@QueryParam("query") String query, @QueryParam("watchableType") String watchableType) {
		
		Filter filter = new Filter(page, pageSize);
		filter.addValue(CustomFilterKey.NAME, query);
		filter.addValue(CustomFilterKey.WATCHABLE_TYPES, WatchableType.findByNames(watchableType));
		return marshallSimple(watchableService.searchWatchable(filter));
	}
	
	@GET
	@Path("latest")
	@Produces(MediaType.APPLICATION_JSON)
	@GZIP
	public String searchLatestWatchables(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize) {
		
		// TODO Improve these suggestions
		Filter filter = new Filter(page, pageSize);
		filter.addValues(CustomFilterKey.WATCHABLE_TYPES, WatchableType.MOVIE, WatchableType.SERIES);
		filter.addValue(CustomFilterKey.IMAGE_REQUIRED, true);
		filter.addValue(CustomFilterKey.RATING_GREATER_THAN, 3);
		return marshallSimple(watchableService.searchWatchable(filter));
	}
}
