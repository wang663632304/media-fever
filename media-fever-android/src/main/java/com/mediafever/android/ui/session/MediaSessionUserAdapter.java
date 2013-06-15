package com.mediafever.android.ui.session;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.images.CustomImageView;
import com.mediafever.R;
import com.mediafever.android.ui.session.MediaSessionUserAdapter.UserCheckeableHolder;
import com.mediafever.domain.UserImpl;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class MediaSessionUserAdapter extends BaseHolderArrayAdapter<UserImpl, UserCheckeableHolder> {
	
	private MediaSession mediaSession;
	
	public MediaSessionUserAdapter(Activity context, List<UserImpl> items, MediaSession mediaSession) {
		super(context, items, R.layout.media_session_user_item);
		this.mediaSession = mediaSession;
	}
	
	@Override
	protected void fillHolderFromItem(final UserImpl user, UserCheckeableHolder holder) {
		holder.image.setImageContent(user.getImage(), R.drawable.user_default);
		holder.fullName.setText(user.getFullname());
		
		if (isAcceptedUser(user)) {
			holder.accepted.setText(R.string.accepted);
			holder.accepted.setVisibility(View.VISIBLE);
		} else if (isPendingUser(user)) {
			holder.accepted.setText(R.string.pending);
			holder.accepted.setVisibility(View.VISIBLE);
		} else {
			holder.accepted.setVisibility(View.GONE);
		}
		holder.checkbox.setOnCheckedChangeListener(null);
		holder.checkbox.setChecked(mediaSession.containsUser(user));
		holder.checkbox.setEnabled(isUserEnabled(user));
		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mediaSession.addUser(user);
				} else {
					mediaSession.removeUser(user);
				}
			}
		});
	}
	
	@Override
	protected UserCheckeableHolder createViewHolderFromConvertView(View convertView) {
		UserCheckeableHolder holder = new UserCheckeableHolder();
		holder.image = findView(convertView, R.id.image);
		holder.fullName = findView(convertView, R.id.fullName);
		holder.accepted = findView(convertView, R.id.accepted);
		holder.checkbox = findView(convertView, R.id.checkbox);
		return holder;
	}
	
	public static class UserCheckeableHolder {
		
		protected CustomImageView image;
		protected TextView fullName;
		protected TextView accepted;
		protected CheckBox checkbox;
	}
	
	protected abstract Boolean isUserEnabled(UserImpl user);
	
	protected abstract Boolean isAcceptedUser(UserImpl user);
	
	protected abstract Boolean isPendingUser(UserImpl user);
	
}
