package com.mediafever.android.ui.session;

import java.util.Date;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.DatePickerDialogFragment.OnDateSetListener;
import com.jdroid.android.fragment.TimePickerDialogFragment.OnTimeSetListener;
import com.jdroid.android.view.DateButton;
import com.jdroid.android.view.TimeButton;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.R;
import com.mediafever.domain.session.MediaSession;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.usecase.mediasession.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionSetupFragment extends AbstractFragment implements OnDateSetListener, OnTimeSetListener {
	
	@InjectView(R.id.movies)
	private CheckBox movies;
	
	@InjectView(R.id.series)
	private CheckBox series;
	
	@InjectView(R.id.date)
	private DateButton dateEditText;
	
	@InjectView(R.id.time)
	private TimeButton timeEditText;
	
	@InjectView(R.id.anyDate)
	private CheckBox anyDate;
	
	@InjectView(R.id.anyTime)
	private CheckBox anyTime;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.media_session_setup_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		MediaSession mediaSession = getMediaSessionSetupUseCase().getMediaSession();
		movies.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					getMediaSessionSetupUseCase().addWatchableType(WatchableType.MOVIE);
				} else {
					getMediaSessionSetupUseCase().removeWatchableType(WatchableType.MOVIE);
				}
			}
		});
		movies.setChecked(mediaSession.getWatchableTypes().contains(WatchableType.MOVIE));
		movies.setEnabled((mediaSession.getId() != null) && movies.isChecked() ? false : true);
		
		series.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					getMediaSessionSetupUseCase().addWatchableType(WatchableType.SERIES);
				} else {
					getMediaSessionSetupUseCase().removeWatchableType(WatchableType.SERIES);
				}
			}
		});
		series.setChecked(mediaSession.getWatchableTypes().contains(WatchableType.SERIES));
		series.setEnabled((mediaSession.getId() != null) && series.isChecked() ? false : true);
		
		Date now = DateUtils.now();
		
		// Date
		anyDate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					dateEditText.setVisibility(View.GONE);
					getMediaSessionSetupUseCase().setDate(null);
				} else {
					dateEditText.setVisibility(View.VISIBLE);
					getMediaSessionSetupUseCase().setDate(dateEditText.getDate());
				}
			}
		});
		if (mediaSession.getDate() != null) {
			dateEditText.init(this, mediaSession.getDate());
			dateEditText.setVisibility(View.VISIBLE);
			anyDate.setChecked(false);
		} else {
			dateEditText.init(this, now);
			dateEditText.setVisibility(View.GONE);
			anyDate.setChecked(true);
		}
		
		// Time
		anyTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					timeEditText.setVisibility(View.GONE);
					getMediaSessionSetupUseCase().setTime(null);
				} else {
					timeEditText.setVisibility(View.VISIBLE);
					getMediaSessionSetupUseCase().setTime(timeEditText.getTime());
				}
			}
		});
		if (mediaSession.getTime() != null) {
			timeEditText.init(this, mediaSession.getTime());
			timeEditText.setVisibility(View.VISIBLE);
			anyTime.setChecked(false);
		} else {
			timeEditText.init(this, now);
			timeEditText.setVisibility(View.GONE);
			anyTime.setChecked(true);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.DatePickerDialogFragment.OnDateSetListener#onDateSet(java.util.Date, int)
	 */
	@Override
	public void onDateSet(Date date, int requestCode) {
		dateEditText.setDate(date);
		getMediaSessionSetupUseCase().setDate(date);
	}
	
	/**
	 * @see com.jdroid.android.fragment.TimePickerDialogFragment.OnTimeSetListener#onTimeSet(java.util.Date)
	 */
	@Override
	public void onTimeSet(Date time) {
		timeEditText.setTime(time);
		getMediaSessionSetupUseCase().setTime(time);
	}
	
	public MediaSessionSetupUseCase getMediaSessionSetupUseCase() {
		return ((MediaSessionActivity)getActivity()).getMediaSessionSetupUseCase();
	}
}
