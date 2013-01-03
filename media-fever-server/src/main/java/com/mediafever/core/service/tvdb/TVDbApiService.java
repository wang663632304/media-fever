package com.mediafever.core.service.tvdb;

import java.util.List;
import org.springframework.stereotype.Service;
import com.jdroid.java.api.AbstractApiService;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.mock.JsonMockWebService;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.repository.PeopleRepository;
import com.mediafever.core.service.tvdb.parser.SeriesParser;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class TVDbApiService extends AbstractApiService {
	
	private static final String SERIES_MODULE = "series";
	
	private static final String COMMON_URL = ApplicationContext.get().getTvApiKey();
	
	public Series getSeries(Long seriesId, PeopleRepository peopleRepository) {
		// Example URL: http://www.thetvdb.com/api/A587336A3A152892/series/79168/all/en.zip
		// Example banners: http://www.thetvdb.com/banners/posters/79168-3.jpg
		WebService webService = newGetService(SERIES_MODULE, seriesId.toString());
		return webService.execute(new SeriesParser(peopleRepository));
	}
	
	/**
	 * @see com.jdroid.java.api.AbstractApiService#getServerURL()
	 */
	@Override
	protected String getServerURL() {
		return ApplicationContext.get().getTvApiURL();
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
		builder.append(COMMON_URL);
		builder.append(StringUtils.SLASH);
		builder.append(urlSegments[0]);
		builder.append(StringUtils.SLASH);
		builder.append(urlSegments[1]);
		builder.append("/all/en.zip");
		return builder.toString();
	}
}
