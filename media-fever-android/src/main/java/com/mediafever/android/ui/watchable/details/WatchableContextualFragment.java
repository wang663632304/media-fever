package com.mediafever.android.ui.watchable.details;

import roboguice.inject.InjectView;
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
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableContextualFragment extends AbstractFragment {
	
	@InjectView(R.id.image)
	private CustomImageView image;
	
	@InjectView(R.id.overview)
	private Button overview;
	
	@InjectView(R.id.viewTrailer)
	private Button viewTrailer;
	
	private UserWatchable<Watchable> userWatchable;
	
	public WatchableContextualFragment() {
	}
	
	public WatchableContextualFragment(UserWatchable<Watchable> userWatchable) {
		this.userWatchable = userWatchable;
	}
	
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
		return inflater.inflate(R.layout.watchable_contextual_fragment, container, false);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Watchable watchable = userWatchable.getWatchable();
		image.setImageContent(watchable.getImage(), R.drawable.watchable_default);
		
		Button seasons = findView(R.id.seasons);
		WatchableType watchableType = WatchableType.find(watchable);
		if (watchableType.equals(WatchableType.MOVIE)) {
			final Movie movie = Movie.class.cast(watchable);
			if (movie.hasYoutubeTrailer()) {
				viewTrailer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ActivityLauncher.launchViewActivity(movie.getTrailerURL());
					}
				});
				viewTrailer.setVisibility(View.VISIBLE);
			}
			seasons.setVisibility(View.GONE);
		} else if (watchableType.equals(WatchableType.SERIES)) {
			seasons.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("unchecked")
				@Override
				public void onClick(View v) {
					((OnItemSelectedListener<TabAction>)getActivity()).onItemSelected(WatchableContextualItem.SEASONS);
				}
			});
		}
		
		overview.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				((OnItemSelectedListener<TabAction>)getActivity()).onItemSelected(WatchableContextualItem.OVERVIEW);
			}
		});
		
	}
}