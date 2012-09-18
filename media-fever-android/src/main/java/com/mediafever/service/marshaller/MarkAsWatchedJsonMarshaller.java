package com.mediafever.service.marshaller;

import java.util.List;
import java.util.Map;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.mediafever.service.marshaller.MarkAsWatchedJsonMarshaller.MarkAsWatched;

/**
 * 
 * @author Maxi Rosson
 */
public class MarkAsWatchedJsonMarshaller implements Marshaller<MarkAsWatched, JsonMap> {
	
	private static final String USER_ID = "userId";
	private static final String WATCHABLES_IDS = "watchablesIds";
	private static final String WATCHED = "watched";
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(MarkAsWatched markAsWatched, MarshallerMode mode, Map<String, String> extras) {
		JsonMap map = new JsonMap(mode, extras);
		map.put(USER_ID, markAsWatched.getUserId());
		map.put(WATCHABLES_IDS, markAsWatched.getWatchablesIds());
		map.put(WATCHED, markAsWatched.getWatched());
		return map;
	}
	
	public static class MarkAsWatched {
		
		private Long userId;
		private List<Long> watchablesIds;
		private Boolean watched;
		
		public MarkAsWatched(Long userId, List<Long> watchablesIds, Boolean watched) {
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
