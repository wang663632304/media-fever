package com.mediafever.core.service.tvdb.parser;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdroid.java.parser.xml.XmlParser;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.NumberUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.core.domain.watchable.Episode;
import com.mediafever.core.domain.watchable.Genre;
import com.mediafever.core.domain.watchable.Person;
import com.mediafever.core.domain.watchable.Season;
import com.mediafever.core.domain.watchable.Series;
import com.mediafever.core.repository.PeopleRepository;

/**
 * 
 * @author Maxi Rosson
 */
public class SeriesDetailsParser extends XmlParser {
	
	// Common
	private static final String ID_TAG = "id";
	private static final String OVERVIEW_TAG = "Overview";
	private static final String LAST_UPDATED_TAG = "lastupdated";
	private static final String RELEASE_YEAR_TAG = "FirstAired";
	private static final String RATING_TAG = "Rating";
	private static final String RATING_COUNT_TAG = "RatingCount";
	private static final String IMAGES_BASE_URL = "http://www.thetvdb.com/banners/";
	
	// Series
	private static final String ACTION_AND_ADVENTURE = "Action and Adventure";
	private static final String SERIES_TAG = "Series";
	private static final String NAME_TAG = "SeriesName";
	private static final String POSTER_TAG = "poster";
	private static final String ACTORS_TAG = "Actors";
	private static final String GENRE_TAG = "Genre";
	
	// Episode
	private static final String EPISODE_TAG = "Episode";
	private static final String EPISODE_NAME_TAG = "EpisodeName";
	private static final String EPISODE_NUMBER_TAG = "EpisodeNumber";
	private static final String SEASON_NUMBER_TAG = "SeasonNumber";
	private static final String FILENAME_TAG = "filename";
	private static final String SEASON_ID_TAG = "seasonid";
	
	// Common
	private Long id;
	private String name;
	private String imageURL;
	private Long lastUpdated;
	private String overview;
	private Date releaseDate;
	private Float rating;
	private Integer ratingCount;
	
	// Series
	private Series series;
	private Boolean onSeries = false;
	private List<Person> actors = Lists.newArrayList();
	private List<Genre> genres = Lists.newArrayList();
	private Map<Long, Season> seasonsMap = Maps.newHashMap();
	
	// Episode
	private Boolean onEpisode = false;
	private Integer episodeNumber;
	private Integer seasonNumber;
	private Long seasonId;
	
	private PeopleRepository peopleRepository;
	
	public SeriesDetailsParser(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}
	
	private void reset() {
		id = null;
		name = null;
		imageURL = null;
		lastUpdated = null;
		overview = null;
		episodeNumber = null;
		seasonNumber = null;
		seasonId = null;
	}
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#onStartElement(java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	protected void onStartElement(String localName, Attributes attributes) {
		if (localName.equals(SERIES_TAG)) {
			onSeries = true;
		} else if (localName.equals(EPISODE_TAG)) {
			onEpisode = true;
		}
	}
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#onEndElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected void onEndElement(String localName, String content) {
		
		if (localName.equals(ID_TAG)) {
			id = NumberUtils.getLong(content);
		} else if (localName.equals(NAME_TAG) || localName.equals(EPISODE_NAME_TAG)) {
			name = content;
		} else if (localName.equals(POSTER_TAG) || localName.equals(FILENAME_TAG)) {
			imageURL = StringUtils.isNotBlank(content) ? IMAGES_BASE_URL + content : null;
		} else if (localName.equals(OVERVIEW_TAG)) {
			overview = content;
		} else if (localName.equals(RELEASE_YEAR_TAG)) {
			releaseDate = StringUtils.isNotEmpty(content) ? DateUtils.parse(content, DateUtils.YYYYMMDD_DATE_FORMAT)
					: null;
		} else if (localName.equals(RATING_TAG)) {
			rating = NumberUtils.getFloat(content, 0f) / 2;
		} else if (localName.equals(RATING_COUNT_TAG)) {
			ratingCount = NumberUtils.getInteger(content);
		} else if (onSeries && localName.equals(GENRE_TAG)) {
			String[] genresNames = content.split("\\|");
			for (int i = 0; i < genresNames.length; i++) {
				String genreName = genresNames[i];
				if (StringUtils.isNotBlank(genreName)) {
					
					if (genreName.equals(ACTION_AND_ADVENTURE)) {
						genres.add(Genre.ACTION);
						genres.add(Genre.ADVENTURE);
					} else {
						Genre genre = Genre.findByName(genreName);
						if (genre != null) {
							genres.add(genre);
						}
					}
				}
			}
		} else if (onSeries && localName.equals(ACTORS_TAG)) {
			String[] actorsNames = content.split("\\|");
			for (int i = 0; i < actorsNames.length; i++) {
				String actorName = actorsNames[i];
				if (StringUtils.isNotBlank(actorName)) {
					
					Person actor = null;
					try {
						actor = peopleRepository.getByFullname(actorName);
					} catch (ObjectNotFoundException e) {
						actor = new Person(actorName);
					}
					actors.add(actor);
				}
			}
		} else if (localName.equals(LAST_UPDATED_TAG)) {
			lastUpdated = NumberUtils.getLong(content);
		} else if (onEpisode && localName.equals(EPISODE_NUMBER_TAG)) {
			episodeNumber = NumberUtils.getInteger(content);
		} else if (onEpisode && localName.equals(SEASON_NUMBER_TAG)) {
			seasonNumber = NumberUtils.getInteger(content);
		} else if (onEpisode && localName.equals(SEASON_ID_TAG)) {
			seasonId = NumberUtils.getLong(content);
		} else if (localName.equals(SERIES_TAG)) {
			onSeries = false;
			series = new Series(id, name, imageURL, overview, actors, genres, rating, ratingCount, releaseDate,
					lastUpdated);
			reset();
		} else if (localName.equals(EPISODE_TAG)) {
			onEpisode = false;
			Season season = seasonsMap.get(seasonId);
			if (season == null) {
				season = new Season(seasonId, seasonNumber);
				series.addSeason(season);
				seasonsMap.put(seasonId, season);
			}
			season.addEpisode(new Episode(id, name, imageURL, overview, rating, ratingCount, releaseDate, lastUpdated,
					episodeNumber));
			reset();
		}
	}
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#getResponse()
	 */
	@Override
	protected Object getResponse() {
		return StringUtils.isNotBlank(series.getName()) ? series : null;
	}
}
