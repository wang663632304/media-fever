package com.mediafever.api.controller.parser;

import java.util.List;
import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.jdroid.javaweb.utils.CSVUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class MarkAsWatchedParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String USER_ID = "userId";
	private static final String WATCHABLES_IDS = "watchablesIds";
	private static final String WATCHED = "watched";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		// TODO This is just a patch. We should fix the way we send the array
		String ids = json.getString(WATCHABLES_IDS).toString();
		ids = ids.replace("[", "");
		ids = ids.replace("]", "");
		List<Long> watchableLongIds = CSVUtils.fromCSV(ids, CSVUtils.LongConverter.get());
		return new MarkAsWatchedJson(json.optLong(USER_ID), watchableLongIds, json.getBoolean(WATCHED));
	}
	
	public class MarkAsWatchedJson {
		
		private Long userId;
		private List<Long> watchablesIds;
		private Boolean watched;
		
		public MarkAsWatchedJson(Long userId, List<Long> watchablesIds, Boolean watched) {
			this.userId = userId;
			this.watchablesIds = watchablesIds;
			this.watched = watched;
		}
		
		public Long getUserId() {
			return userId;
		}
		
		public List<Long> getWatchablesIds() {
			return watchablesIds;
		}
		
		public Boolean getWatched() {
			return watched;
		}
	}
}
