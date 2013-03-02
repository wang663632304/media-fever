package com.mediafever.android.ui.session;

import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.domain.User;
import com.jdroid.android.images.BorderedCustomImageView;
import com.jdroid.android.utils.AndroidDateUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.ui.session.MediaSessionAdapter.MediaSessionHolder;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.session.MediaSessionUser;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionAdapter extends BaseHolderArrayAdapter<MediaSession, MediaSessionHolder> {
	
	private User user;
	
	public MediaSessionAdapter(Activity context, List<MediaSession> items, User user) {
		super(context, items, R.layout.media_session_item);
		this.user = user;
	}
	
	@Override
	protected void fillHolderFromItem(final MediaSession mediaSession, MediaSessionHolder holder) {
		holder.watchableTypes.setText(getWatchablesString(mediaSession));
		String dateTime = getDateString(mediaSession);
		if (StringUtils.isNotEmpty(dateTime)) {
			holder.date.setText(dateTime);
			holder.date.setVisibility(View.VISIBLE);
		} else {
			holder.date.setVisibility(View.INVISIBLE);
		}
		
		holder.selections.removeAllViews();
		holder.usersUp.removeAllViews();
		holder.usersDown.removeAllViews();
		
		List<MediaSelection> firstSelections = mediaSession.getTop3Selections();
		for (MediaSelection mediaSelection : firstSelections) {
			BorderedCustomImageView borderedCustomImageView = new BorderedCustomImageView(getContext(),
					R.drawable.watchable_default, R.dimen.mediaSessionSelectionWidth,
					R.dimen.mediaSessionSelectionHeight);
			borderedCustomImageView.setImageContent(mediaSelection.getWatchable().getImage(),
				R.drawable.watchable_default);
			holder.selections.addView(borderedCustomImageView);
		}
		
		int max = 4;
		if (AndroidUtils.isLargeScreenOrBigger()) {
			max = 8;
		}
		
		int usersAdded = 0;
		for (MediaSessionUser mediaSessionUser : mediaSession.getUsers()) {
			if (usersAdded < max) {
				User user = mediaSessionUser.getUser();
				if (!user.getId().equals(this.user.getId())) {
					BorderedCustomImageView borderedCustomImageView = new BorderedCustomImageView(getContext(),
							R.drawable.user_default, R.dimen.mediaSessionUserImageDim, R.dimen.mediaSessionUserImageDim);
					borderedCustomImageView.setImageContent(user.getImage(), R.drawable.user_default);
					if (usersAdded < (max / 2)) {
						holder.usersUp.addView(borderedCustomImageView);
					} else {
						holder.usersDown.addView(borderedCustomImageView);
					}
					usersAdded++;
				}
			}
		}
	}
	
	private String getWatchablesString(MediaSession mediaSession) {
		if (mediaSession.acceptOnlyMovies()) {
			return getContext().getString(R.string.movies);
		} else if (mediaSession.acceptOnlySeries()) {
			return getContext().getString(R.string.series);
		} else {
			return getContext().getString(R.string.moviesAndSeries);
		}
	}
	
	public static String getDateString(MediaSession mediaSession) {
		
		Date date = mediaSession.getDate();
		Date time = mediaSession.getTime();
		StringBuilder dateBuilder = new StringBuilder();
		if (date != null) {
			
			Date fullDate = DateUtils.getDate(date, time);
			
			Date now = DateUtils.now();
			long absSecondsToDate = Math.abs((fullDate.getTime() - now.getTime()) / 1000);
			Boolean isCurrentDay = DateUtils.getDay(now) == DateUtils.getDay(fullDate);
			
			if ((absSecondsToDate < DateUtils.DAY) && isCurrentDay) {
				dateBuilder.append(LocalizationUtils.getString(R.string.todayDateFormat));
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
		holder.selections = findView(convertView, R.id.selections);
		holder.usersUp = findView(convertView, R.id.usersUp);
		holder.usersDown = findView(convertView, R.id.usersDown);
		return holder;
	}
	
	public static class MediaSessionHolder {
		
		protected TextView watchableTypes;
		protected TextView date;
		private LinearLayout selections;
		private LinearLayout usersUp;
		private LinearLayout usersDown;
	}
	
}
