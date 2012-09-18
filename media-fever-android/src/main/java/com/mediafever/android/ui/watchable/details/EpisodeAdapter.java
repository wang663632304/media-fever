package com.mediafever.android.ui.watchable.details;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.details.EpisodeAdapter.EpisodeUserWatchableHolder;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Episode;

/**
 * 
 * @author Maxi Rosson
 */
public class EpisodeAdapter extends BaseHolderArrayAdapter<UserWatchable<Episode>, EpisodeUserWatchableHolder> {
	
	public EpisodeAdapter(Activity context, List<UserWatchable<Episode>> items) {
		super(context, items, R.layout.episode_item);
	}
	
	@Override
	protected void fillHolderFromItem(UserWatchable<Episode> episodeUserWatchable, EpisodeUserWatchableHolder holder) {
		holder.number.setText(episodeUserWatchable.getWatchable().getEpisodeNumber().toString());
		holder.name.setText(episodeUserWatchable.getWatchable().getName());
		holder.watched.setVisibility(episodeUserWatchable.isWatched() ? View.VISIBLE : View.GONE);
	}
	
	@Override
	protected EpisodeUserWatchableHolder createViewHolderFromConvertView(View convertView) {
		EpisodeUserWatchableHolder holder = new EpisodeUserWatchableHolder();
		holder.number = findView(convertView, R.id.number);
		holder.name = findView(convertView, R.id.name);
		holder.watched = findView(convertView, R.id.watched);
		return holder;
	}
	
	public static class EpisodeUserWatchableHolder {
		
		protected TextView number;
		protected TextView name;
		protected ImageView watched;
	}
	
}
