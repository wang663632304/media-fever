package com.mediafever.core.service.moviedb.parser;

import java.util.List;
import org.json.JSONException;
import com.google.common.collect.Lists;
import com.jdroid.java.parser.json.JsonArrayWrapper;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.NumberUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.core.domain.watchable.Genre;
import com.mediafever.core.domain.watchable.Movie;
import com.mediafever.core.domain.watchable.Person;
import com.mediafever.core.repository.PeopleRepository;

/**
 * 
 * @author Maxi Rosson
 */
public class MovieParser extends JsonParser<JsonArrayWrapper> {
	
	private static final String NOTHING_FOUND = "[\"Nothing found.\"]";
	
	private static final String ID_KEY = "id";
	private static final String NAME_KEY = "name";
	private static final String TAGLINE_KEY = "tagline";
	private static final String OVERVIEW_KEY = "overview";
	private static final String TRAILER_KEY = "trailer";
	private static final String RELEASED_KEY = "released";
	private static final String LAST_MODIFIED_AT_KEY = "last_modified_at";
	private static final String POSTERS_KEY = "posters";
	private static final String IMAGE_KEY = "image";
	private static final String GENRES_KEY = "genres";
	private static final String VOTES_KEY = "votes";
	private static final String RATING_KEY = "rating";
	private static final String CAST_KEY = "cast";
	private static final String DEPARTMENT_KEY = "department";
	private static final String ACTORS_KEY = "Actors";
	private static final String MOVIE_TYPE_KEY = "movie_type";
	private static final String MOVIE = "movie";
	
	private final static String NO_OVERVIEW_FOUND = "No overview found.";
	
	private PeopleRepository peopleRepository;
	
	public MovieParser(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonArrayWrapper json) throws JSONException {
		
		Movie movie = null;
		if (!json.toString().equals(NOTHING_FOUND)) {
			
			JsonObjectWrapper jsonObject = json.getJSONObject(0);
			
			if (MOVIE.equalsIgnoreCase(jsonObject.getString(MOVIE_TYPE_KEY))) {
				
				Long id = jsonObject.getLong(ID_KEY);
				String name = jsonObject.getString(NAME_KEY);
				String tagline = jsonObject.optString(TAGLINE_KEY);
				String overview = jsonObject.getString(OVERVIEW_KEY);
				if (NO_OVERVIEW_FOUND.equals(overview)) {
					overview = null;
				}
				String trailerURL = jsonObject.getString(TRAILER_KEY);
				String released = jsonObject.getString(RELEASED_KEY);
				Integer releaseYear = null;
				if (StringUtils.isNotBlank(released)) {
					releaseYear = NumberUtils.getInteger(released.split("-")[0]);
				}
				Float rating = jsonObject.optFloat(RATING_KEY, 0f) / 2;
				Integer ratingCount = jsonObject.getInt(VOTES_KEY);
				
				Long lastupdated = jsonObject.getDate(LAST_MODIFIED_AT_KEY, DateUtils.YYYYMMDDHHMMSSZ_DATE_FORMAT).getTime();
				
				String imageURL = null;
				JsonArrayWrapper postersArray = jsonObject.getJSONArray(POSTERS_KEY);
				if (postersArray.length() > 0) {
					imageURL = postersArray.getJSONObject(0).getJSONObject(IMAGE_KEY).getString("url");
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
				
				JsonArrayWrapper castArray = jsonObject.getJSONArray(CAST_KEY);
				List<Person> actors = Lists.newArrayList();
				for (int i = 0; i < castArray.length(); i++) {
					String department = castArray.getJSONObject(i).getString(DEPARTMENT_KEY);
					if (department.equals(ACTORS_KEY)) {
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
				}
				
				movie = new Movie(id, name, imageURL, overview, actors, genres, rating, ratingCount, releaseYear,
						lastupdated, tagline, trailerURL);
			}
		}
		return movie;
	}
}
