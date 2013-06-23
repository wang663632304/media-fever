package com.mediafever.core.service.moviedb.parser;

import java.util.Date;
import java.util.List;
import org.json.JSONException;
import com.google.common.collect.Lists;
import com.jdroid.java.parser.json.JsonArrayWrapper;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.core.domain.watchable.Genre;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.domain.watchable.Person;
import com.mediafever.core.repository.PeopleRepository;

/**
 * 
 * @author Maxi Rosson
 */
public class MovieParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String NOTHING_FOUND = "{\"status_code\"";
	
	private static final String ID_KEY = "id";
	private static final String ORIGINAL_TITLE_KEY = "original_title";
	private static final String NAME_KEY = "name";
	private static final String TAGLINE_KEY = "tagline";
	private static final String OVERVIEW_KEY = "overview";
	private static final String TRAILERS_KEY = "trailers";
	private static final String RELEASED_KEY = "release_date";
	private static final String RATING_KEY = "vote_average";
	private static final String VOTES_KEY = "vote_count";
	private static final String POSTERS_KEY = "poster_path";
	private static final String GENRES_KEY = "genres";
	private static final String CASTS_KEY = "casts";
	private static final String CAST_KEY = "cast";
	
	private final static String NO_OVERVIEW_FOUND = "No overview found.";
	
	private PeopleRepository peopleRepository;
	
	private String baseImageURL;
	private String baseTrailerURL;
	
	public MovieParser(String baseImageURL, String baseTrailerURL, PeopleRepository peopleRepository) {
		this.baseImageURL = baseImageURL;
		this.baseTrailerURL = baseTrailerURL;
		this.peopleRepository = peopleRepository;
	}
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper jsonObject) throws JSONException {
		
		Movie movie = null;
		if (!jsonObject.toString().contains(NOTHING_FOUND)) {
			
			Long id = jsonObject.getLong(ID_KEY);
			String name = jsonObject.getString(ORIGINAL_TITLE_KEY);
			String tagline = jsonObject.optString(TAGLINE_KEY);
			String overview = jsonObject.getString(OVERVIEW_KEY);
			if (NO_OVERVIEW_FOUND.equals(overview)) {
				overview = null;
			}
			Date releaseDate = jsonObject.optDate(RELEASED_KEY, DateUtils.YYYYMMDD_DATE_FORMAT);
			Float rating = jsonObject.optFloat(RATING_KEY, 0f) / 2;
			Integer ratingCount = jsonObject.getInt(VOTES_KEY);
			String imageURL = baseImageURL + jsonObject.getString(POSTERS_KEY);
			
			JsonObjectWrapper trailerJson = jsonObject.getJSONObject(TRAILERS_KEY);
			JsonArrayWrapper trailers = trailerJson.getJSONArray("youtube");
			String trailerURL = null;
			if (trailers.length() > 0) {
				trailerURL = baseTrailerURL + trailers.getJSONObject(0).getString("source");
			}
			
			JsonArrayWrapper genresArray = jsonObject.getJSONArray(GENRES_KEY);
			List<Genre> genres = Lists.newArrayList();
			for (int i = 0; i < genresArray.length(); i++) {
				String genreName = genresArray.getJSONObject(i).getString(NAME_KEY);
				Genre genre = Genre.findByName(genreName);
				if (genre != null) {
					genres.add(genre);
				}
			}
			
			JsonArrayWrapper castArray = jsonObject.getJSONObject(CASTS_KEY).getJSONArray(CAST_KEY);
			List<Person> actors = Lists.newArrayList();
			for (int i = 0; i < castArray.length(); i++) {
				String fullname = castArray.getJSONObject(i).getString(NAME_KEY);
				Person actor = null;
				try {
					actor = peopleRepository.getByFullname(fullname);
				} catch (ObjectNotFoundException e) {
					actor = new Person(fullname);
				}
				if (!actors.contains(actor)) {
					actors.add(actor);
				}
			}
			
			movie = new Movie(id, name, imageURL, overview, actors, genres, rating, ratingCount, releaseDate,
					new Date().getTime(), tagline, trailerURL);
		}
		return movie;
	}
}
