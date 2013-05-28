package com.mediafever.api.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.java.context.GitContext;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.scheduler.SynchronizationScheduler;

/**
 * @author Maxi Rosson
 */
@Path("admin")
@Controller
public class AdminController {
	
	@Autowired
	private SynchronizationScheduler synchronizationScheduler;
	
	@GET
	@Path("info")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAppInfo() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("App Name: ");
		builder.append(ApplicationContext.get().getAppName());
		builder.append("\nApp Version: ");
		builder.append(ApplicationContext.get().getAppVersion());
		builder.append("\nApp URL: ");
		builder.append(ApplicationContext.get().getAppURL());
		builder.append("\nAPI Version: ");
		builder.append(ApplicationContext.get().getApiVersion());
		builder.append("\nCommit Id: ");
		builder.append(GitContext.get().getCommitId());
		builder.append("\nCommit Time: ");
		builder.append(GitContext.get().getCommitTime());
		builder.append("\nCommit Build Time: ");
		builder.append(GitContext.get().getBuildTime());
		builder.append("\nHttp Mock Enabled: ");
		builder.append(ApplicationContext.get().isHttpMockEnabled());
		builder.append("\nHttp Mock Sleep Duration: ");
		builder.append(ApplicationContext.get().getHttpMockSleepDuration());
		builder.append("\nMovies API URL: ");
		builder.append(ApplicationContext.get().getMoviesApiURL());
		builder.append("\nMovies API Key: ");
		builder.append(ApplicationContext.get().getMoviesApiKey());
		builder.append("\nTV API URL: ");
		builder.append(ApplicationContext.get().getTvApiURL());
		builder.append("\nTV API Key: ");
		builder.append(ApplicationContext.get().getTvApiKey());
		return builder.toString();
	}
	
	@GET
	@Path("synchMovies")
	@Produces(MediaType.TEXT_PLAIN)
	public String synchMovies() {
		if (synchronizationScheduler.startSyncMoviesSchedule()) {
			return "Movies synchronization started. Latest sync finished on "
					+ synchronizationScheduler.getLastMoviesSyncEndDate();
		}
		return "Movies synchronization already in progress. Start time: "
				+ synchronizationScheduler.getLastMoviesSyncStartDate();
	}
	
	@GET
	@Path("synchSeries")
	@Produces(MediaType.TEXT_PLAIN)
	public String synchSeries() {
		if (synchronizationScheduler.startSyncSeriesSchedule()) {
			return "Series synchronization started. Latest sync finished on "
					+ synchronizationScheduler.getLastSeriesSyncEndDate();
		}
		return "Series synchronization already in progress. Start time: "
				+ synchronizationScheduler.getLastSeriesSyncStartDate();
	}
}
