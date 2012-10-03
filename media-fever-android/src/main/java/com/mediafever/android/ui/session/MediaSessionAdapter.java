package com.mediafever.android.ui.session;

import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.ui.session.MediaSessionAdapter.MediaSessionHolder;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionAdapter extends BaseHolderArrayAdapter<MediaSession, MediaSessionHolder> {
	
	private static final String SEPARATOR = ", ";
	
	public MediaSessionAdapter(Activity context, List<MediaSession> items) {
		super(context, items, R.layout.media_session_item);
	}
	
	@Override
	protected void fillHolderFromItem(final MediaSession mediaSession, MediaSessionHolder holder) {
		holder.watchableTypes.setText(StringUtils.join(mediaSession.getWatchableTypes(), SEPARATOR));
		holder.date.setText(getDateString(mediaSession.getDate()));
	}
	
	private String getDateString(Date date) {
		
		Date now = DateUtils.now();
		long absSecondsToDate = Math.abs((date.getTime() - now.getTime()) / 1000);
		Boolean isCurrentDay = DateUtils.getDay(now) == DateUtils.getDay(date);
		
		if ((absSecondsToDate < DateUtils.DAY) && isCurrentDay) {
			return getContext().getString(R.string.todayDateFormat,
				DateUtils.format(date, DateUtils.HHMMAA_DATE_FORMAT));
		} else if (absSecondsToDate < DateUtils.WEEK) {
			return DateUtils.format(date, DateUtils.EHHMMAA_DATE_FORMAT);
		} else {
			Boolean isCurrentYear = DateUtils.getYear(now) == DateUtils.getYear(date);
			return DateUtils.format(date, isCurrentYear ? DateUtils.MMMDHHMMAA_DATE_FORMAT
					: DateUtils.MMMDYYYYHHMMAA_DATE_FORMAT);
		}
	}
	
	@Override
	protected MediaSessionHolder createViewHolderFromConvertView(View convertView) {
		MediaSessionHolder holder = new MediaSessionHolder();
		holder.watchableTypes = findView(convertView, R.id.watchableTypes);
		holder.date = findView(convertView, R.id.date);
		return holder;
	}
	
	public static class MediaSessionHolder {
		
		protected TextView watchableTypes;
		protected TextView date;
	}
	
}
