package com.mediafever.parser;

import java.util.Date;
import java.util.List;
import org.json.JSONException;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.session.MediaSessionUser;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String DATE = "date";
	private static final String TIME = "time";
	private static final String USERS = "users";
	private static final String SELECTIONS = "selections";
	private static final String WATCHABLES_TYPES = "watchablesTypes";
	
	private MediaSession mediaSession;
	
	public MediaSessionParser(MediaSession mediaSession) {
		this.mediaSession = mediaSession;
	}
	
	public MediaSessionParser() {
		this(null);
	}
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		Long id = json.getLong(ID);
		Date date = json.getDate(DATE);
		Date time = json.getDate(TIME);
		List<MediaSessionUser> users = parseList(json.getJSONArray(USERS), new MediaSessionUserParser());
		
		List<MediaSelection> selections = null;
		if (json.has(SELECTIONS)) {
			selections = parseList(json.getJSONArray(SELECTIONS), new MediaSelectionParser(users));
		}
		List<WatchableType> watchablesTypes = Lists.newArrayList();
		for (String watchableType : parseListString(json.getJSONArray(WATCHABLES_TYPES))) {
			watchablesTypes.add(WatchableType.find(watchableType));
		}
		Boolean accepted = null;
		for (MediaSessionUser user : users) {
			if (SecurityContext.get().getUser().equals(user.getUser())) {
				accepted = user.isAccepted();
				break;
			}
		}
		
		if (mediaSession != null) {
			mediaSession.setDate(date);
			mediaSession.setTime(time);
			mediaSession.setUsers(users);
			mediaSession.setSelections(selections);
			mediaSession.setWatchableTypes(watchablesTypes);
			mediaSession.setAccepted(accepted);
			return mediaSession;
		} else {
			return new MediaSession(id, date, time, users, selections, watchablesTypes, accepted);
		}
	}
}