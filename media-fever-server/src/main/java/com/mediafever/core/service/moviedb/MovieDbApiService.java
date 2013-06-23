package com.mediafever.core.service.moviedb;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jdroid.java.api.AbstractApacheApiService;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.MimeType;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.mock.JsonMockWebService;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.repository.PeopleRepository;
import com.mediafever.core.repository.SettingsRepository;
import com.mediafever.core.service.moviedb.parser.LatestParser;
import com.mediafever.core.service.moviedb.parser.MovieParser;
import com.mediafever.core.service.moviedb.parser.UpdatedMovieIdsParser;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class MovieDbApiService extends AbstractApacheApiService {
	
	@Autowired
	private SettingsRepository settingsRepository;
	
	private static final String ACCEPT_HEADER_KEY = "Accept";
	
	private static final String MOVIE_MODULE = "movie";
	private static final String GET_CHANGES_ACTION = "changes";
	private static final String GET_LATEST_ACTION = "latest";
	
	private static final String API_KEY_PARAMETER = "api_key";
	private static final String PAGE_PARAMETER = "page";
	private static final String APPEND_TO_RESPONSE_PARAMETER = "append_to_response";
	
	public Movie getMovie(Long movieId, PeopleRepository peopleRepository) {
		// Example URL: http://api.themoviedb.org/3/movie/MOVIE_ID?api_key=APIKEY&append_to_response=trailers,casts
		// Add the append to response parameter to call to trailers and cast APIs in a single call.
		WebService webService = newGetService(MOVIE_MODULE, movieId);
		webService.addQueryParameter(APPEND_TO_RESPONSE_PARAMETER, "trailers,casts");
		return webService.execute(new MovieParser(settingsRepository.getMovieImageBaseURL(),
				settingsRepository.getMovieTrailerBaseURL(), peopleRepository));
	}
	
	/**
	 * @return the ID of the last movie created in the db
	 */
	public Long getLatest() {
		// Example URL: http://api.themoviedb.org/3/movie/latest?api_key=APIKEY
		WebService webService = newGetService(MOVIE_MODULE, GET_LATEST_ACTION);
		return webService.execute(new LatestParser());
	}
	
	/**
	 * @return List of IDs that belong to the movies that have been updated on the last 24hs.
	 */
	public List<Long> getUpdatedMovieIds() {
		// Example URL: http://api.themoviedb.org/3/movie/changes?api_key=APIKEY&page=3
		UpdatedMovieIdsParser parser = new UpdatedMovieIdsParser();
		
		WebService webService = newGetService(MOVIE_MODULE, GET_CHANGES_ACTION);
		webService.execute(parser);
		
		// Since this API is paginated, we need to call it for all pages.
		for (int i = 2; i <= parser.getTotalPages(); i++) {
			webService = newGetService(MOVIE_MODULE, GET_CHANGES_ACTION);
			webService.addQueryParameter(PAGE_PARAMETER, i);
			webService.execute(parser);
		}
		return parser.getMovieIds();
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
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#newGetService(java.lang.Boolean, java.lang.Object[])
	 */
	@Override
	protected WebService newGetService(Boolean mocked, Object... urlSegments) {
		WebService webService = super.newGetService(mocked, urlSegments);
		webService.addQueryParameter(API_KEY_PARAMETER, ApplicationContext.get().getMoviesApiKey());
		webService.addHeader(ACCEPT_HEADER_KEY, MimeType.JSON.toString());
		return webService;
	}
}
