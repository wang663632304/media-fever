package com.mediafever.android.ui;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.images.CustomImageView;
import com.mediafever.R;
import com.mediafever.android.ui.UserAdapter.UserHolder;
import com.mediafever.domain.UserImpl;

/**
 * 
 * @author Maxi Rosson
 */
public class UserAdapter extends BaseHolderArrayAdapter<UserImpl, UserHolder> {
	
	private Boolean clickable = true;
	
	public UserAdapter(Activity context, List<UserImpl> items, int resource) {
		super(context, items, resource);
	}
	
	public UserAdapter(Activity context, List<UserImpl> items, int resource, Boolean clickable) {
		super(context, items, resource);
		this.clickable = clickable;
	}
	
	public UserAdapter(Activity context, List<UserImpl> items) {
		super(context, items, R.layout.user_item);
	}
	
	@Override
	protected void fillHolderFromItem(UserImpl user, UserHolder holder) {
		holder.image.setImageContent(user.getImage(), R.drawable.user_default);
		holder.fullName.setText(user.getFullname());
	}
	
	@Override
	protected UserHolder createViewHolderFromConvertView(View convertView) {
		UserHolder holder = new UserHolder();
		holder.image = findView(convertView, R.id.image);
		holder.fullName = findView(convertView, R.id.fullName);
		return holder;
	}
	
	/**
	 * @see android.widget.BaseAdapter#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int position) {
		return clickable;
	}
	
	public static class UserHolder {
		
		protected CustomImageView image;
		protected TextView fullName;
	}
	
}
