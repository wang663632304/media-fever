package com.mediafever.android.ui;

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
import com.mediafever.android.ui.UserCheckeableAdapter.UserCheckeableHolder;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class UserCheckeableAdapter extends BaseHolderArrayAdapter<UserImpl, UserCheckeableHolder> {
	
	public UserCheckeableAdapter(Activity context, List<UserImpl> items, int resource) {
		super(context, items, resource);
	}
	
	public UserCheckeableAdapter(Activity context, List<UserImpl> items) {
		super(context, items, R.layout.user_checkeable_item);
	}
	
	@Override
	protected void fillHolderFromItem(final UserImpl user, UserCheckeableHolder holder) {
		holder.image.setImageContent(user.getImage(), R.drawable.user_default);
		holder.fullName.setText(user.getFullname());
		holder.checkbox.setOnCheckedChangeListener(null);
		holder.checkbox.setChecked(isUserChecked(user));
		holder.checkbox.setEnabled(isUserEnabled(user));
		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					onUserChecked(user);
				} else {
					onUserUnChecked(user);
				}
			}
		});
	}
	
	@Override
	protected UserCheckeableHolder createViewHolderFromConvertView(View convertView) {
		UserCheckeableHolder holder = new UserCheckeableHolder();
		holder.image = findView(convertView, R.id.image);
		holder.fullName = findView(convertView, R.id.fullName);
		holder.checkbox = findView(convertView, R.id.checkbox);
		return holder;
	}
	
	public static class UserCheckeableHolder {
		
		protected CustomImageView image;
		protected TextView fullName;
		protected CheckBox checkbox;
	}
	
	protected abstract void onUserChecked(UserImpl user);
	
	protected abstract void onUserUnChecked(UserImpl user);
	
	protected abstract Boolean isUserChecked(UserImpl user);
	
	protected abstract Boolean isUserEnabled(UserImpl user);
	
}
