package com.mediafever.api.controller.parser;

import java.util.Date;
import java.util.List;
import org.json.JSONException;
import com.jdroid.java.collections.Lists;
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
	private static final String TIME = "time";
	private static final String WATCHABLE_TYPE = "watchableTypes";
	private static final String USER_IDS = "usersIds";
	private static final String WATCHABLES_IDS = "watchablesIds";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		// TODO This is just a patch. We should fix the way we send the array
		List<WatchableType> watchableTypes = null;
		if (json.has(WATCHABLE_TYPE)) {
			String csv = json.getString(WATCHABLE_TYPE).toString();
			csv = csv.replace("[", "");
			csv = csv.replace("]", "");
			csv = csv.replace(" ", "");
			watchableTypes = WatchableType.findByNames(csv);
		}
		
		List<Long> usersIds = Lists.newArrayList();
		if (json.has(USER_IDS)) {
			String csv = json.getString(USER_IDS).toString();
			csv = csv.replace("[", "");
			csv = csv.replace("]", "");
			usersIds = CSVUtils.fromCSV(csv, CSVUtils.LongConverter.get());
		}
		
		List<Long> watchablesIds = Lists.newArrayList();
		if (json.has(WATCHABLES_IDS)) {
			String csv = json.getString(WATCHABLES_IDS).toString();
			csv = csv.replace("[", "");
			csv = csv.replace("]", "");
			watchablesIds = CSVUtils.fromCSV(csv, CSVUtils.LongConverter.get());
		}
		
		return new MediaSessionJson(json.getDate(DATE), json.getDate(TIME), watchableTypes, usersIds, watchablesIds);
	}
	
	public class MediaSessionJson {
		
		private Date date;
		private Date time;
		private List<WatchableType> watchableTypes;
		private List<Long> usersIds;
		private List<Long> watchablesIds;
		
		public MediaSessionJson(Date date, Date time, List<WatchableType> watchableTypes, List<Long> usersIds,
				List<Long> watchablesIds) {
			this.date = date;
			this.time = time;
			this.watchableTypes = watchableTypes;
			this.usersIds = usersIds;
			this.watchablesIds = watchablesIds;
		}
		
		public Date getTime() {
			return time;
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
		
		public List<Long> getWatchablesIds() {
			return watchablesIds;
		}
	}
}
