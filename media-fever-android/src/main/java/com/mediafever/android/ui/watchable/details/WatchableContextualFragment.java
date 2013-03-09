package com.mediafever.android.ui.watchable.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.fragment.OnItemSelectedListener;
import com.jdroid.android.images.CustomImageView;
import com.jdroid.android.tabs.TabAction;
import com.mediafever.R;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Movie;
import com.mediafever.domain.watchable.Series;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableContextualFragment extends AbstractFragment {
	
	private static final String USER_WATCHABLE_EXTRA = "userWatchable";
	
	private UserWatchable<Watchable> userWatchable;
	
	public static WatchableContextualFragment instance(UserWatchable<Watchable> userWatchable) {
		WatchableContextualFragment fragment = new WatchableContextualFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(USER_WATCHABLE_EXTRA, userWatchable);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		userWatchable = getArgument(USER_WATCHABLE_EXTRA);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.watchable_contextual_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Watchable watchable = userWatchable.getWatchable();
		
		CustomImageView image = findView(R.id.image);
		image.setImageContent(watchable.getImage(), R.drawable.watchable_default);
		
		Button seasons = findView(R.id.seasonsButton);
		WatchableType watchableType = WatchableType.find(watchable);
		if (watchableType.equals(WatchableType.MOVIE)) {
			final Movie movie = Movie.class.cast(watchable);
			if (movie.hasYoutubeTrailer()) {
				
				Button viewTrailer = findView(R.id.viewTrailerButton);
				viewTrailer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ActivityLauncher.launchViewActivity(movie.getTrailerURL());
					}
				});
				viewTrailer.setVisibility(View.VISIBLE);
			}
		} else if (watchableType.equals(WatchableType.SERIES)) {
			if (((Series)watchable).hasSeasons()) {
				seasons.setOnClickListener(new OnClickListener() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View v) {
						((OnItemSelectedListener<TabAction>)getActivity()).onItemSelected(WatchableContextualItem.SEASONS);
					}
				});
				seasons.setVisibility(View.VISIBLE);
			}
		}
		
		findView(R.id.overviewButton).setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				((OnItemSelectedListener<TabAction>)getActivity()).onItemSelected(WatchableContextualItem.OVERVIEW);
			}
		});
		
		findView(R.id.socialButton).setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				((OnItemSelectedListener<TabAction>)getActivity()).onItemSelected(WatchableContextualItem.SOCIAL);
			}
		});
		
		findView(R.id.mediaSessionButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Implement this
			}
		});
	}
}
