package com.mediafever.core.service.moviedb.parser;

import java.util.List;
import org.json.JSONException;
import com.google.common.collect.Lists;
import com.jdroid.java.parser.json.JsonArrayWrapper;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * 
 * @author Maxi Rosson
 */
public class UpdatedMovieIdsParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID_KEY = "id";
	private static final String RESULTS_KEY = "results";
	private static final String TOTAL_PAGES_KEY = "total_pages";
	
	private Integer totalPages;
	private List<Long> movieIds = Lists.newArrayList();
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		
		totalPages = json.getInt(TOTAL_PAGES_KEY);
		JsonArrayWrapper jsonArray = json.getJSONArray(RESULTS_KEY);
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JsonObjectWrapper jsonUpdatedMovie = jsonArray.getJSONObject(i);
			movieIds.add(jsonUpdatedMovie.getLong(ID_KEY));
		}
		return movieIds;
	}
	
	/**
	 * @return the totalPages
	 */
	public Integer getTotalPages() {
		return totalPages;
	}
	
	/**
	 * @return the movieIds
	 */
	public List<Long> getMovieIds() {
		return movieIds;
	}
}
