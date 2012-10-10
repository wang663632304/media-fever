package com.mediafever.api.controller.parser;

import java.util.Date;
import java.util.List;
import org.json.JSONException;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.jdroid.javaweb.utils.CSVUtils;
import com.mediafever.core.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String DATE = "date";
	private static final String WATCHABLE_TYPE = "watchableTypes";
	private static final String USER_IDS = "usersIds";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		// TODO This is just a patch. We should fix the way we send the array
		String csv = json.getString(WATCHABLE_TYPE).toString();
		csv = csv.replace("[", "");
		csv = csv.replace("]", "");
		List<WatchableType> watchableTypes = WatchableType.findByNames(csv);
		
		csv = json.getString(USER_IDS).toString();
		csv = csv.replace("[", "");
		csv = csv.replace("]", "");
		List<Long> usersIds = CSVUtils.fromCSV(csv, CSVUtils.LongConverter.get());
		
		return new MediaSessionJson(json.getDate(DATE), watchableTypes, usersIds);
	}
	
	public class MediaSessionJson {
		
		private Date date;
		
		private List<WatchableType> watchableTypes;
		private List<Long> usersIds;
		
		public MediaSessionJson(Date date, List<WatchableType> watchableTypes, List<Long> usersIds) {
			this.date = date;
			this.watchableTypes = watchableTypes;
			this.usersIds = usersIds;
		}
		
		public Date getDate() {
			return date;
		}
		
		public void setDate(Date date) {
			this.date = date;
		}
		
		public List<WatchableType> getWatchableTypes() {
			return watchableTypes;
		}
		
		public void setWatchableTypes(List<WatchableType> watchableTypes) {
			this.watchableTypes = watchableTypes;
		}
		
		public List<Long> getUsersIds() {
			return usersIds;
		}
		
		public void setUsersIds(List<Long> usersIds) {
			this.usersIds = usersIds;
		}
	}
}
