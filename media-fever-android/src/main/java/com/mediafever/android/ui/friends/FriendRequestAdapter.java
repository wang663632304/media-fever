package com.mediafever.android.ui.friends;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.images.CustomImageView;
import com.mediafever.R;
import com.mediafever.android.ui.friends.FriendRequestAdapter.FriendRequestHolder;
import com.mediafever.domain.FriendRequest;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class FriendRequestAdapter extends BaseHolderArrayAdapter<FriendRequest, FriendRequestHolder> {
	
	public FriendRequestAdapter(Activity context, List<FriendRequest> items) {
		super(context, items, R.layout.friend_request_item);
	}
	
	@Override
	protected void fillHolderFromItem(final FriendRequest friendRequest, FriendRequestHolder holder) {
		holder.image.setImageContent(friendRequest.getSender().getImage(), R.drawable.user_default);
		holder.fullName.setText(friendRequest.getSender().getFullname());
		holder.accept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onAccept(friendRequest);
			}
		});
		holder.reject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onReject(friendRequest);
			}
		});
	}
	
	public abstract void onAccept(FriendRequest friendRequest);
	
	public abstract void onReject(FriendRequest friendRequest);
	
	@Override
	protected FriendRequestHolder createViewHolderFromConvertView(View convertView) {
		FriendRequestHolder holder = new FriendRequestHolder();
		holder.image = findView(convertView, R.id.image);
		holder.fullName = findView(convertView, R.id.fullName);
		holder.accept = findView(convertView, R.id.accept);
		holder.reject = findView(convertView, R.id.reject);
		return holder;
	}
	
	public static class FriendRequestHolder {
		
		protected CustomImageView image;
		protected TextView fullName;
		protected View accept;
		protected View reject;
	}
	
}
