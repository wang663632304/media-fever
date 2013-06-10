package com.mediafever.core.service.tvdb;

import java.util.List;
import org.springframework.stereotype.Service;
import com.jdroid.java.api.AbstractApacheApiService;
import com.jdroid.java.http.HttpWebServiceProcessor;
import com.jdroid.java.http.WebService;
import com.jdroid.java.http.mock.AbstractMockWebService;
import com.jdroid.java.http.mock.JsonMockWebService;
import com.jdroid.java.parser.zip.ZipFileParser;
import com.mediafever.context.ApplicationContext;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.repository.PeopleRepository;
import com.mediafever.core.service.tvdb.parser.SeriesDetailsParser;
import com.mediafever.core.service.tvdb.parser.SeriesInitialUpdateParser;
import com.mediafever.core.service.tvdb.parser.SeriesUpdateParser;
import com.mediafever.core.service.tvdb.parser.SeriesUpdateResponse;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class TVDbApiService extends AbstractApacheApiService {
	
	private static final String SERIES_MODULE = "series";
	private static final String UPDATES_MODULE = "Updates.php";
	private static final String UPDATE_MODULE = "updates";
	
	private static final String COMMON_URL = ApplicationContext.get().getTvApiKey();
	
	public Series getSeries(Long seriesId, PeopleRepository peopleRepository) {
		// Example URL: http://www.thetvdb.com/api/A587336A3A152892/series/79168/all/en.zip
		// Example banners: http://www.thetvdb.com/banners/posters/79168-3.jpg
		WebService webService = newGetService(COMMON_URL, SERIES_MODULE, seriesId.toString(), "all/en.zip");
		return webService.execute(new ZipFileParser(new SeriesDetailsParser(peopleRepository), "en.xml"));
	}
	
	public SeriesUpdateResponse getUpdatedSeries(String lastUpdate) {
		WebService webService = newGetService(UPDATES_MODULE);
		webService.addQueryParameter("type", "series");
		webService.addQueryParameter("time", lastUpdate);
		
		return webService.execute(new SeriesUpdateParser());
	}
	
	public SeriesUpdateResponse getAllSeries() {
		// Example URL: http://www.thetvdb.com/api/A587336A3A152892/updates/updates_all.zip
		WebService webService = newGetService(COMMON_URL, UPDATE_MODULE, "updates_all.zip");
		return webService.execute(new ZipFileParser(new SeriesInitialUpdateParser(), "updates_all.xml"));
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
}
