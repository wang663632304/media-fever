package com.mediafever.parser;

import java.util.Date;
import java.util.List;
import org.json.JSONException;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.domain.watchingsession.WatchingSession;
import com.mediafever.domain.watchingsession.WatchingSessionUser;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String USERS = "users";
	private static final String WATCHABLES_TYPES = "watchablesTypes";
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		Long id = json.getLong(ID);
		Date date = json.getDate(DATE);
		List<WatchingSessionUser> users = parseList(json.getJSONArray(USERS), new WatchingSessionUserParser());
		List<WatchableType> watchablesTypes = Lists.newArrayList();
		for (String watchableType : parseListString(json.getJSONArray(WATCHABLES_TYPES))) {
			watchablesTypes.add(WatchableType.find(watchableType));
		}
		Boolean accepted = null;
		for (WatchingSessionUser user : users) {
			if (SecurityContext.get().getUser().equals(user.getUser())) {
				accepted = user.isAccepted();
				break;
			}
		}
		return new WatchingSession(id, date, users, watchablesTypes, accepted);
	}
}