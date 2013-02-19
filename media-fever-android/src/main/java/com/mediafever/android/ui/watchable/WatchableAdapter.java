package com.mediafever.android.ui.watchable;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.images.CustomImageView;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.WatchableAdapter.WatchableHolder;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableAdapter extends BaseHolderArrayAdapter<Watchable, WatchableHolder> {
	
	private static final String SEPARATOR = " - ";
	
	public WatchableAdapter(Activity context, List<Watchable> items) {
		super(context, items, R.layout.watchable_item);
	}
	
	@Override
	protected void fillHolderFromItem(Watchable watchable, WatchableHolder holder) {
		holder.name.setText(watchable.getName());
		holder.image.setImageContent(watchable.getImage(), R.drawable.watchable_default);
		if (holder.description != null) {
			holder.description.setText(WatchableAdapter.getWatchableDescription(watchable));
		}
	}
	
	@Override
	protected WatchableHolder createViewHolderFromConvertView(View convertView) {
		WatchableHolder holder = new WatchableHolder();
		holder.name = findView(convertView, R.id.name);
		holder.image = findView(convertView, R.id.image);
		holder.description = findView(convertView, R.id.description);
		return holder;
	}
	
	public static class WatchableHolder {
		
		protected CustomImageView image;
		protected TextView name;
		protected TextView description;
	}
	
	public static String getWatchableDescription(Watchable watchable) {
		return LocalizationUtils.getString(WatchableType.find(watchable).getResourceId())
				+ (watchable.getReleaseYear() != null ? SEPARATOR + watchable.getReleaseYear() : StringUtils.EMPTY);
	}
	
}
