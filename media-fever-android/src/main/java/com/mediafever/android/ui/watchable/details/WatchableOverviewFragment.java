package com.mediafever.android.ui.watchable.details;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.images.CustomImageView;
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
public class WatchableOverviewFragment extends AbstractFragment {
	
	private static final String USER_WATCHABLE_EXTRA = "userWatchable";
	private static final String SEPARATOR = " | ";
	
	@InjectView(R.id.image)
	private CustomImageView image;
	
	@InjectView(R.id.name)
	private TextView name;
	
	@InjectView(R.id.tagline)
	private TextView tagline;
	
	@InjectView(R.id.releaseYear)
	private TextView releaseYear;
	
	@InjectView(R.id.overview)
	private TextView overview;
	
	@InjectView(R.id.overviewTitle)
	private TextView overviewTitle;
	
	@InjectView(R.id.actorsTitle)
	private TextView actorsTitle;
	
	@InjectView(R.id.actors)
	private TextView actors;
	
	@InjectView(R.id.genresTitle)
	private TextView genresTitle;
	
	@InjectView(R.id.genres)
	private TextView genres;
	
	@InjectView(R.id.viewTrailer)
	private Button viewTrailer;
	
	@InjectView(R.id.watchedToogle)
	private Button watchedToogle;
	
	@InjectView(R.id.wishListToogle)
	private Button wishListToogle;
	
	private UpdateUserWatchableUseCase updateUserWatchableUseCase;
	
	private UserWatchable<Watchable> userWatchable;
	
	public WatchableOverviewFragment() {
	}
	
	public WatchableOverviewFragment(UserWatchable<Watchable> userWatchable) {
		this.userWatchable = userWatchable;
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(USER_WATCHABLE_EXTRA, userWatchable);
		setArguments(bundle);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		Bundle args = getArguments();
		if (args != null) {
			userWatchable = (UserWatchable<Watchable>)args.getSerializable(USER_WATCHABLE_EXTRA);
		}
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.watchable_overview_fragment, container, false);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Watchable watchable = userWatchable.getWatchable();
		image.setImageContent(watchable.getImage(), R.drawable.watchable_default);
		name.setText(watchable.getName());
		
		if (watchable.getReleaseYear() != null) {
			releaseYear.setText(watchable.getReleaseYear().toString());
		}
		
		if (StringUtils.isNotEmpty(watchable.getOverview())) {
			overview.setText(watchable.getOverview());
		} else {
			overview.setVisibility(View.GONE);
			overviewTitle.setVisibility(View.GONE);
		}
		
		if (CollectionUtils.isNotEmpty(watchable.getActors())) {
			actors.setText(StringUtils.join(watchable.getActors(), SEPARATOR));
		} else {
			actors.setVisibility(View.GONE);
			actorsTitle.setVisibility(View.GONE);
		}
		
		if (CollectionUtils.isNotEmpty(watchable.getGenres())) {
			genres.setText(StringUtils.join(watchable.getGenres(), SEPARATOR));
		} else {
			genres.setVisibility(View.GONE);
			genresTitle.setVisibility(View.GONE);
		}
		
		if (WatchableType.MOVIE.match(watchable)) {
			final Movie movie = Movie.class.cast(watchable);
			
			if (StringUtils.isNotBlank(movie.getTagline())) {
				tagline.setText(movie.getTagline());
				tagline.setVisibility(View.VISIBLE);
			}
			if (movie.hasYoutubeTrailer()) {
				viewTrailer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailerURL())));
					}
				});
				viewTrailer.setVisibility(View.VISIBLE);
			}
		}
		
		if (updateUserWatchableUseCase == null) {
			updateUserWatchableUseCase = getInstance(UpdateUserWatchableUseCase.class);
			updateUserWatchableUseCase.setUserWatchable(userWatchable);
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
