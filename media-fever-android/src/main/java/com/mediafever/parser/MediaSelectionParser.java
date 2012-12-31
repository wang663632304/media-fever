package com.mediafever.parser;

import java.util.List;
import org.json.JSONException;
import com.jdroid.android.domain.User;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSessionUser;
import com.mediafever.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String ID = "id";
	private static final String WATCHABLE = "watchable";
	private static final String THUMBS_UP_USERS = "thumbsUpUsers";
	private static final String THUMBS_DOWN_USERS = "thumbsDownUsers";
	private static final String OWNER_ID = "ownerId";
	
	private List<MediaSessionUser> mediaSessionUsers;
	
	public MediaSelectionParser(List<MediaSessionUser> mediaSessionUsers) {
		this.mediaSessionUsers = mediaSessionUsers;
	}
	
	/**
	 * @see com.jdroid.java.parser.json.JsonParser#parse(java.lang.Object)
	 */
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		
		Watchable watchable = (Watchable)new WatchableParser().parse(json.getJSONObject(WATCHABLE));
		User owner = findMediaSessionUser(json.getLong(OWNER_ID)).getUser();
		
		List<MediaSessionUser> thumbsUpUsers = Lists.newArrayList();
		for (Long userId : parseListLong(json.optJSONArray(THUMBS_UP_USERS))) {
			thumbsUpUsers.add(findMediaSessionUser(userId));
		}
		
		List<MediaSessionUser> thumbsDownUsers = Lists.newArrayList();
		for (Long userId : parseListLong(json.optJSONArray(THUMBS_DOWN_USERS))) {
			thumbsDownUsers.add(findMediaSessionUser(userId));
		}
		
		return new MediaSelection(json.getLong(ID), watchable, owner, thumbsUpUsers, thumbsDownUsers);
	}
	
	private MediaSessionUser findMediaSessionUser(Long userId) {
		for (MediaSessionUser mediaSessionUser : mediaSessionUsers) {
			if (mediaSessionUser.getUser().getId().equals(userId)) {
				return mediaSessionUser;
			}
		}
		return null;
	}
	
}
