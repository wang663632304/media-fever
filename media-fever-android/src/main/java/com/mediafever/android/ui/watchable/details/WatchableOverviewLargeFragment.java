package com.mediafever.android.ui.watchable.details;

import roboguice.inject.InjectView;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.java.utils.CollectionUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Movie;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.usecase.UpdateUserWatchableUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableOverviewLargeFragment extends AbstractFragment {
	
	private static final String USER_WATCHABLE_EXTRA = "userWatchable";
	private static final String SEPARATOR = " | ";
	
	@InjectView(R.id.name)
	private TextView name;
	
	@InjectView(R.id.tagline)
	private TextView tagline;
	
	@InjectView(R.id.releaseYear)
	private TextView releaseYear;
	
	@InjectView(R.id.overviewContainer)
	private View overviewContainer;
	
	@InjectView(R.id.overview)
	private TextView overview;
	
	@InjectView(R.id.actorsContainer)
	private View actorsContainer;
	
	@InjectView(R.id.actors)
	private TextView actors;
	
	@InjectView(R.id.genresContainer)
	private View genresContainer;
	
	@InjectView(R.id.genres)
	private TextView genres;
	
	@InjectView(R.id.watchedToogle)
	private Button watchedToogle;
	
	@InjectView(R.id.wishListToogle)
	private Button wishListToogle;
	
	private UpdateUserWatchableUseCase updateUserWatchableUseCase;
	
	private UserWatchable<Watchable> userWatchable;
	
	public static WatchableOverviewLargeFragment instance(UserWatchable<Watchable> userWatchable) {
		WatchableOverviewLargeFragment fragment = new WatchableOverviewLargeFragment();
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
		setRetainInstance(true);
		
		userWatchable = getArgument(USER_WATCHABLE_EXTRA);
		updateUserWatchableUseCase = getInstance(UpdateUserWatchableUseCase.class);
		updateUserWatchableUseCase.setUserWatchable(userWatchable);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.watchable_overview_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		Watchable watchable = userWatchable.getWatchable();
		name.setText(watchable.getName());
		
		if (watchable.getReleaseYear() != null) {
			releaseYear.setText(watchable.getReleaseYear().toString());
		}
		
		if (StringUtils.isNotEmpty(watchable.getOverview())) {
			overview.setText(watchable.getOverview());
		} else {
			overviewContainer.setVisibility(View.GONE);
		}
		
		if (CollectionUtils.isNotEmpty(watchable.getActors())) {
			actors.setText(StringUtils.join(watchable.getActors(), SEPARATOR));
		} else {
			actorsContainer.setVisibility(View.GONE);
		}
		
		if (CollectionUtils.isNotEmpty(watchable.getGenres())) {
			genres.setText(StringUtils.join(watchable.getGenres(), SEPARATOR));
		} else {
			genresContainer.setVisibility(View.GONE);
		}
		
		if (WatchableType.MOVIE.match(watchable)) {
			Movie movie = Movie.class.cast(watchable);
			if (StringUtils.isNotBlank(movie.getTagline())) {
				tagline.setText(movie.getTagline());
				tagline.setVisibility(View.VISIBLE);
			}
		}
		
		watchedToogle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUserWatchableUseCase.toogleWatched();
				executeUseCase(updateUserWatchableUseCase);
			}
		});
		
		wishListToogle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUserWatchableUseCase.toogleIsInWishList();
				executeUseCase(updateUserWatchableUseCase);
			}
		});
		refreshToogles();
	}
	
	private void refreshToogles() {
		Drawable rightWatchedDrawable = getResources().getDrawable(
			userWatchable.isWatched() ? R.drawable.accept : R.drawable.reject);
		watchedToogle.setCompoundDrawablesWithIntrinsicBounds(null, null, rightWatchedDrawable, null);
		
		Drawable rightWishLisDrawable = getResources().getDrawable(
			userWatchable.isInWishList() ? R.drawable.accept : R.drawable.reject);
		wishListToogle.setCompoundDrawablesWithIntrinsicBounds(null, null, rightWishLisDrawable, null);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(updateUserWatchableUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(updateUserWatchableUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				userWatchable = updateUserWatchableUseCase.getUserWatchable();
				refreshToogles();
				dismissLoading();
			}
		});
	}
}
