package com.mediafever.android.ui.session;

import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.ui.BorderImage;
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
		if (mediaSession.getDate() != null) {
			holder.date.setText(getDateString(mediaSession.getDate()));
			holder.date.setVisibility(View.VISIBLE);
		} else {
			holder.date.setVisibility(View.GONE);
		}
		
		holder.users.removeAllViews();
		
		int max = Math.min(mediaSession.getUsers().size(), 5);
		if (AndroidUtils.isLargeScreenOrBigger()) {
			max = mediaSession.getUsers().size();
		}
		for (int i = 0; i < max; i++) {
			BorderImage borderImage = new BorderImage(getContext(), R.dimen.rowSamllImageDim, R.dimen.rowSamllImageDim);
			borderImage.setImageContent(mediaSession.getUsers().get(i).getUser().getImage(), R.drawable.user_default);
			holder.users.addView(borderImage);
		}
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
		holder.users = findView(convertView, R.id.users);
		return holder;
	}
	
	public static class MediaSessionHolder {
		
		protected TextView watchableTypes;
		protected TextView date;
		private LinearLayout users;
	}
	
}
