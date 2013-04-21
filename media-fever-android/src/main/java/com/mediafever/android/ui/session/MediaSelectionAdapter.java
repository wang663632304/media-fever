package com.mediafever.android.ui.session;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.images.CustomImageView;
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
		MediaSelectionAdapter.doFillHolderFromItem(mediaSelection, holder);
	}
	
	public static void doFillHolderFromItem(MediaSelection mediaSelection, MediaSelectionHolder holder) {
		Watchable watchable = mediaSelection.getWatchable();
		if (watchable != null) {
			holder.name.setText(watchable.getName());
			holder.name.setVisibility(View.VISIBLE);
			holder.image.setImageContent(watchable.getImage(), R.drawable.watchable_default);
			holder.image.setVisibility(View.VISIBLE);
			
			holder.userContainer.setVisibility(View.VISIBLE);
			holder.userImage.setImageContent(mediaSelection.getOwner().getImage(), R.drawable.user_default);
			holder.fullName.setText(mediaSelection.getOwner().getFullname());
			
			Integer thumbsUp = mediaSelection.getThumbsUpUsers().size();
			if (thumbsUp > 0) {
				holder.thumbsUp.setText(thumbsUp.toString());
				holder.thumbsUp.setVisibility(View.VISIBLE);
				holder.thumbsUpIcon.setVisibility(View.VISIBLE);
			} else {
				holder.thumbsUp.setVisibility(View.GONE);
				holder.thumbsUpIcon.setVisibility(View.GONE);
			}
			
			Integer thumbsDown = mediaSelection.getThumbsDownUsers().size();
			if (thumbsDown > 0) {
				holder.thumbsDown.setText(thumbsDown.toString());
				holder.thumbsDown.setVisibility(View.VISIBLE);
				holder.thumbsDownIcon.setVisibility(View.VISIBLE);
			} else {
				holder.thumbsDown.setVisibility(View.GONE);
				holder.thumbsDownIcon.setVisibility(View.GONE);
			}
			
			holder.addNew.setVisibility(View.GONE);
		} else {
			holder.name.setVisibility(View.GONE);
			holder.image.setVisibility(View.GONE);
			holder.userContainer.setVisibility(View.GONE);
			holder.thumbsUp.setVisibility(View.GONE);
			holder.thumbsUpIcon.setVisibility(View.GONE);
			holder.thumbsDown.setVisibility(View.GONE);
			holder.thumbsDownIcon.setVisibility(View.GONE);
			holder.addNew.setVisibility(View.VISIBLE);
		}
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
		holder.thumbsUp = findView(convertView, R.id.thumbsUp);
		holder.thumbsUpIcon = findView(convertView, R.id.thumbsUpIcon);
		holder.thumbsDown = findView(convertView, R.id.thumbsDown);
		holder.thumbsDownIcon = findView(convertView, R.id.thumbsDownIcon);
		return holder;
	}
	
	public static class MediaSelectionHolder {
		
		protected CustomImageView image;
		protected TextView name;
		protected TextView addNew;
		protected ViewGroup userContainer;
		protected CustomImageView userImage;
		protected TextView fullName;
		protected TextView thumbsUp;
		protected ImageView thumbsUpIcon;
		protected TextView thumbsDown;
		protected ImageView thumbsDownIcon;
	}
}
