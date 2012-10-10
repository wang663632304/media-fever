package com.mediafever.android.ui.session;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.DatePickerDialogFragment.OnDateSetListener;
import com.jdroid.android.fragment.TimePickerDialogFragment.OnTimeSetListener;
import com.jdroid.android.view.DateButton;
import com.jdroid.android.view.TimeButton;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.R;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.usecase.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionSetupFragment extends AbstractFragment implements OnDateSetListener, OnTimeSetListener {
	
	private MediaSessionSetupUseCase mediaSessionSetupUseCase;
	
	@InjectView(R.id.movies)
	private CheckBox movies;
	
	@InjectView(R.id.series)
	private CheckBox series;
	
	@InjectView(R.id.date)
	private DateButton dateEditText;
	
	@InjectView(R.id.time)
	private TimeButton timeEditText;
	
	@InjectView(R.id.defined)
	private RadioButton defined;
	
	@InjectView(R.id.next)
	private Button next;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (mediaSessionSetupUseCase == null) {
			mediaSessionSetupUseCase = getInstance(MediaSessionSetupUseCase.class);
		}
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
		
		Date now = DateUtils.now();
		dateEditText.init(this, now);
		timeEditText.init(this, now);
		
		defined.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				dateEditText.setEnabled(isChecked);
				timeEditText.setEnabled(isChecked);
			}
		});
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				next();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(mediaSessionSetupUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(mediaSessionSetupUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.DatePickerDialogFragment.OnDateSetListener#onDateSet(java.util.Date)
	 */
	@Override
	public void onDateSet(Date date) {
		dateEditText.setDate(date);
	}
	
	/**
	 * @see com.jdroid.android.fragment.TimePickerDialogFragment.OnTimeSetListener#onTimeSet(java.util.Date)
	 */
	@Override
	public void onTimeSet(Date time) {
		timeEditText.setTime(time);
	}
	
	private void next() {
		
		if (defined.isChecked()) {
			Date date = dateEditText.getDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Date time = timeEditText.getTime();
			calendar.set(Calendar.HOUR_OF_DAY, DateUtils.getHour(time, true));
			calendar.set(Calendar.MINUTE, DateUtils.getMinute(time));
			mediaSessionSetupUseCase.setDate(calendar.getTime());
		} else {
			mediaSessionSetupUseCase.setDate(null);
		}
		
		List<WatchableType> watchableTypes = Lists.newArrayList();
		if (movies.isChecked()) {
			watchableTypes.add(WatchableType.MOVIE);
		}
		if (series.isChecked()) {
			watchableTypes.add(WatchableType.SERIES);
		}
		mediaSessionSetupUseCase.setWatchableTypes(watchableTypes);
		
		executeUseCase(mediaSessionSetupUseCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// TODO Change this target activity
		ActivityLauncher.launchHomeActivity();
	}
}
