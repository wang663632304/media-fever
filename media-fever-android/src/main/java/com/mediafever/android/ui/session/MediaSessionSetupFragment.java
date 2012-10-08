package com.mediafever.android.ui.session;

import java.util.Date;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.DatePickerDialogFragment.OnDateSetListener;
import com.jdroid.android.fragment.TimePickerDialogFragment.OnTimeSetListener;
import com.jdroid.android.view.DateButton;
import com.jdroid.android.view.TimeButton;
import com.jdroid.java.utils.DateUtils;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionSetupFragment extends AbstractFragment implements OnDateSetListener, OnTimeSetListener {
	
	@InjectView(R.id.date)
	private DateButton dateEditText;
	
	@InjectView(R.id.time)
	private TimeButton timeEditText;
	
	@InjectView(R.id.defined)
	private RadioButton defined;
	
	@InjectView(R.id.notDefined)
	private RadioButton notDefined;
	
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
}