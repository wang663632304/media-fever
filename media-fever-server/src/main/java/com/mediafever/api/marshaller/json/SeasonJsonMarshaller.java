package com.mediafever.api.marshaller.json;

import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.jdroid.java.json.JsonMap;
import com.jdroid.java.marshaller.Marshaller;
import com.jdroid.java.marshaller.MarshallerMode;
import com.jdroid.javaweb.guava.function.IdPropertyFunction;
import com.mediafever.core.domain.UserWatchable;
import com.mediafever.core.domain.watchable.Season;
import com.mediafever.core.service.UserWatchableService;

/**
 * 
 * @author Maxi Rosson
 */
public class SeasonJsonMarshaller implements Marshaller<Season, JsonMap> {
	
	public static final String USER_ID_EXTRA = "userId";
	
	private static final String ID = "id";
	private static final String NUMBER = "number";
	private static final String EPISODES_USER_WATCHABLES = "episodesUserWatchables";
	
	private UserWatchableService userWatchableService;
	
	/**
	 * @see com.jdroid.java.marshaller.Marshaller#marshall(java.lang.Object,
	 *      com.jdroid.java.marshaller.MarshallerMode, java.util.Map)
	 */
	@Override
	public JsonMap marshall(Season season, MarshallerMode mode, Map<String, String> extras) {
		
		List<Long> watchablesIds = Lists.transform(season.getEpisodes(), new IdPropertyFunction());
		
		JsonMap map = new JsonMap(mode, extras);
		map.put(ID, season.getId());
		map.put(NUMBER, season.getSeasonNumber());
		
		Long userId = Long.parseLong(extras.get(USER_ID_EXTRA));
		List<UserWatchable> userWatchables = userWatchableService.getEpisodeUserWatchables(userId, watchablesIds);
		map.put(EPISODES_USER_WATCHABLES, userWatchables);
		return map;
	}
	
	public void setUserWatchableService(UserWatchableService userWatchableService) {
		this.userWatchableService = userWatchableService;
	}
}
