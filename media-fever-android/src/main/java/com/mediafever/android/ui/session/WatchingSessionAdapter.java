package com.mediafever.android.ui.session;

import java.util.Date;
import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.java.utils.DateUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.ui.session.WatchingSessionAdapter.WatchingSessionHolder;
import com.mediafever.domain.watchingsession.WatchingSession;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchingSessionAdapter extends BaseHolderArrayAdapter<WatchingSession, WatchingSessionHolder> {
	
	private static final String SEPARATOR = ", ";
	
	public WatchingSessionAdapter(Activity context, List<WatchingSession> items) {
		super(context, items, R.layout.watching_session_item);
	}
	
	@Override
	protected void fillHolderFromItem(final WatchingSession watchingSession, WatchingSessionHolder holder) {
		holder.watchableTypes.setText(StringUtils.join(watchingSession.getWatchableTypes(), SEPARATOR));
		holder.date.setText(getDateString(watchingSession.getDate()));
		
		if (watchingSession.isAccepted()) {
			holder.accept.setVisibility(View.GONE);
			holder.reject.setVisibility(View.GONE);
		} else {
			holder.accept.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onAccept(watchingSession);
				}
			});
			holder.accept.setVisibility(View.VISIBLE);
			
			holder.reject.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onReject(watchingSession);
				}
			});
			holder.reject.setVisibility(View.VISIBLE);
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
	
	public void onAccept(WatchingSession watchingSession) {
		
	};
	
	public void onReject(WatchingSession watchingSession) {
		
	};
	
	@Override
	protected WatchingSessionHolder createViewHolderFromConvertView(View convertView) {
		WatchingSessionHolder holder = new WatchingSessionHolder();
		holder.watchableTypes = findView(convertView, R.id.watchableTypes);
		holder.accept = findView(convertView, R.id.accept);
		holder.reject = findView(convertView, R.id.reject);
		holder.date = findView(convertView, R.id.date);
		return holder;
	}
	
	public static class WatchingSessionHolder {
		
		protected TextView watchableTypes;
		protected View accept;
		protected View reject;
		protected TextView date;
	}
	
}
