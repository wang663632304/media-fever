package com.mediafever.android.ui.session;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.images.CustomImageView;
import com.jdroid.java.utils.CollectionUtils;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.android.ui.session.MediaSelectionAdapter.MediaSelectionHolder;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionAdapter extends BaseHolderArrayAdapter<MediaSelection, MediaSelectionHolder> {
	
	public MediaSelectionAdapter(Activity context, List<MediaSelection> items) {
		super(context, items, R.layout.media_selection_item);
	}
	
	@Override
	protected void fillHolderFromItem(MediaSelection mediaSelection, MediaSelectionHolder holder) {
		Watchable watchable = mediaSelection.getWatchable();
		if (watchable != null) {
			holder.name.setText(mediaSelection.getWatchable().getName());
			holder.name.setVisibility(View.VISIBLE);
			holder.image.setImageContent(mediaSelection.getWatchable().getImage(), R.drawable.watchable_default);
			holder.image.setVisibility(View.VISIBLE);
			
			holder.userContainer.setVisibility(View.VISIBLE);
			holder.userImage.setImageContent(mediaSelection.getOwner().getImage(), R.drawable.user_default);
			holder.fullName.setText(mediaSelection.getOwner().getFullname());
			
			String thumbs = getThumbs(mediaSelection);
			holder.thumbs.setText(thumbs);
			holder.thumbs.setVisibility(StringUtils.isBlank(thumbs) ? View.GONE : View.VISIBLE);
			
			holder.addNew.setVisibility(View.GONE);
		} else {
			holder.name.setVisibility(View.GONE);
			holder.image.setVisibility(View.GONE);
			holder.userContainer.setVisibility(View.GONE);
			holder.thumbs.setVisibility(View.GONE);
			holder.addNew.setVisibility(View.VISIBLE);
		}
	}
	
	private String getThumbs(MediaSelection mediaSelection) {
		StringBuilder builder = new StringBuilder();
		if (CollectionUtils.isNotEmpty(mediaSelection.getThumbsUpUsers())) {
			builder.append(mediaSelection.getThumbsUpUsers().size());
			builder.append("+");
		}
		if (CollectionUtils.isNotEmpty(mediaSelection.getThumbsDownUsers())) {
			if (builder.length() > 0) {
				builder.append(StringUtils.EMPTY);
			}
			builder.append(mediaSelection.getThumbsDownUsers().size());
			builder.append("-");
		}
		return builder.toString();
	}
	
	@Override
	protected MediaSelectionHolder createViewHolderFromConvertView(View convertView) {
		MediaSelectionHolder holder = new MediaSelectionHolder();
		holder.name = findView(convertView, R.id.name);
		holder.image = findView(convertView, R.id.image);
		holder.addNew = findView(convertView, R.id.addNew);
		holder.userContainer = findView(convertView, R.id.userContainer);
		holder.userImage = findView(convertView, R.id.userImage);
		holder.fullName = findView(convertView, R.id.fullName);
		holder.thumbs = findView(convertView, R.id.thumbs);
		return holder;
	}
	
	public static class MediaSelectionHolder {
		
		protected CustomImageView image;
		protected TextView name;
		protected TextView addNew;
		protected ViewGroup userContainer;
		protected CustomImageView userImage;
		protected TextView fullName;
		protected TextView thumbs;
	}
}
