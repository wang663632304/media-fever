package com.mediafever.core.service.moviedb;

import java.util.List;
import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import com.jdroid.java.api.AbstractApiService;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.mock.JsonMockWebService;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.guava.function.PropertyFunction;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.domain.watchable.Watchable;
import com.mediafever.core.repository.PeopleRepository;
import com.mediafever.core.service.moviedb.parser.GetVersionParser;
import com.mediafever.core.service.moviedb.parser.LatestParser;
import com.mediafever.core.service.moviedb.parser.MovieParser;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class MovieDbApiService extends AbstractApiService {
	
	private static final String MOVIE_MODULE = "Movie";
	private static final String GET_INFO_ACTION = "getInfo";
	private static final String GET_LATEST_ACTION = "getLatest";
	private static final String GET_VERSION_ACTION = "getVersion";
	
	private static final String COMMON_URL = "/en/json/" + ApplicationContext.get().getMoviesApiKey();
	
	public Movie getMovie(Long movieId, PeopleRepository peopleRepository) {
		WebService webService = newGetService(MOVIE_MODULE, GET_INFO_ACTION);
		webService.addUrlSegment(movieId);
		return webService.execute(new MovieParser(peopleRepository));
	}
	
	/**
	 * @return the ID of the last movie created in the db
	 */
	public Long getLatest() {
		// Example URL: http://api.themoviedb.org/2.1/Movie.getLatest/en/json/APIKEY
		WebService webService = newGetService(MOVIE_MODULE, GET_LATEST_ACTION);
		return webService.execute(new LatestParser());
	}
	
	public List<Long> getVersion(List<Watchable> movies) {
		// Example URL: http://api.themoviedb.org/2.1/Movie.getVersion/en/json/APIKEY/585,155,11,550
		WebService webService = newGetService(MOVIE_MODULE, GET_VERSION_ACTION);
		
		List<Long> externalIds = Lists.transform(movies, new PropertyFunction<Watchable, Long>("externalId"));
		webService.addUrlSegment(StringUtils.join(externalIds));
		return webService.execute(new GetVersionParser(movies));
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getServerURL()
	 */
	@Override
	protected String getServerURL() {
		return ApplicationContext.get().getMoviesApiURL();
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getHttpWebServiceProcessors()
	 */
	@Override
	protected List<HttpWebServiceProcessor> getHttpWebServiceProcessors() {
		return null;
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getAbstractMockWebServiceInstance(java.lang.Object[])
	 */
	@Override
	protected AbstractMockWebService getAbstractMockWebServiceInstance(Object... urlSegments) {
		return new JsonMockWebService(urlSegments) {
			
			@Override
			protected Integer getHttpMockSleepDuration(Object... urlSegments) {
				return ApplicationContext.get().getHttpMockSleepDuration();
			}
		};
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#isHttpMockEnabled()
	 */
	@Override
	protected Boolean isHttpMockEnabled() {
		return ApplicationContext.get().isHttpMockEnabled();
	}
	
	@Override
	protected String getBaseURL(String serverUrl, Object... urlSegments) {
		StringBuilder builder = new StringBuilder();
		builder.append(serverUrl);
		builder.append(SEPARATOR);
		builder.append(urlSegments[0]);
		builder.append(".");
		builder.append(urlSegments[1]);
		builder.append(COMMON_URL);
		return builder.toString();
	}
}
