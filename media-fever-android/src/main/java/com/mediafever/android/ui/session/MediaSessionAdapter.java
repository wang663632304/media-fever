package com.mediafever.android.ui.session;

import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.domain.User;
import com.jdroid.android.utils.AndroidDateUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.ui.BorderImage;
import com.mediafever.android.ui.session.MediaSessionAdapter.MediaSessionHolder;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.session.MediaSessionUser;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionAdapter extends BaseHolderArrayAdapter<MediaSession, MediaSessionHolder> {
	
	private static final String SEPARATOR = ", ";
	private User user;
	
	public MediaSessionAdapter(Activity context, List<MediaSession> items, User user) {
		super(context, items, R.layout.media_session_item);
		this.user = user;
	}
	
	@Override
	protected void fillHolderFromItem(final MediaSession mediaSession, MediaSessionHolder holder) {
		holder.watchableTypes.setText(StringUtils.join(mediaSession.getWatchableTypes(), SEPARATOR));
		if ((mediaSession.getDate() != null) || (mediaSession.getTime() != null)) {
			holder.date.setText(getDateString(mediaSession.getDate(), mediaSession.getTime()));
			holder.date.setVisibility(View.VISIBLE);
		} else {
			holder.date.setVisibility(View.INVISIBLE);
		}
		
		holder.users.removeAllViews();
		
		int max = 5;
		if (AndroidUtils.isLargeScreenOrBigger()) {
			max = mediaSession.getUsers().size();
		}
		
		int usersAdded = 0;
		for (MediaSessionUser mediaSessionUser : mediaSession.getUsers()) {
			if (usersAdded < max) {
				User user = mediaSessionUser.getUser();
				if (!user.getId().equals(this.user.getId())) {
					BorderImage borderImage = new BorderImage(getContext(), R.dimen.rowSamllImageDim,
							R.dimen.rowSamllImageDim);
					borderImage.setImageContent(user.getImage(), R.drawable.user_default);
					holder.users.addView(borderImage);
					usersAdded++;
				}
			}
		}
	}
	
	private String getDateString(Date date, Date time) {
		
		StringBuilder dateBuilder = new StringBuilder();
		if (date != null) {
			
			Date fullDate = DateUtils.getDate(date, time);
			
			Date now = DateUtils.now();
			long absSecondsToDate = Math.abs((fullDate.getTime() - now.getTime()) / 1000);
			Boolean isCurrentDay = DateUtils.getDay(now) == DateUtils.getDay(fullDate);
			
			if ((absSecondsToDate < DateUtils.DAY) && isCurrentDay) {
				dateBuilder.append(getContext().getString(R.string.todayDateFormat));
			} else if (absSecondsToDate < DateUtils.WEEK) {
				dateBuilder.append(DateUtils.format(fullDate, DateUtils.E_DATE_FORMAT));
			} else {
				Boolean isCurrentYear = DateUtils.getYear(now) == DateUtils.getYear(fullDate);
				dateBuilder.append(DateUtils.format(fullDate, isCurrentYear ? DateUtils.MMMD_DATE_FORMAT
						: DateUtils.MMMDYYYY_DATE_FORMAT));
			}
		}
		if (time != null) {
			if (date != null) {
				dateBuilder.append(StringUtils.SPACE);
			}
			dateBuilder.append(AndroidDateUtils.formatTime(time));
		}
		return dateBuilder.toString();
	}
	
	@Override
	protected MediaSessionHolder createViewHolderFromConvertView(View convertView) {
		MediaSessionHolder holder = new MediaSessionHolder();
		holder.watchableTypes = findView(convertView, R.id.watchableTypes);
		holder.date = findView(convertView, R.id.date);
		holder.users = findView(convertView, R.id.users);
		return holder;
	}
	
	public static class MediaSessionHolder {
		
		protected TextView watchableTypes;
		protected TextView date;
		private LinearLayout users;
	}
	
}
