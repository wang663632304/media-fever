package com.mediafever.android.ui.friends;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.jdroid.android.images.CustomImageView;
import com.mediafever.R;
import com.mediafever.android.ui.friends.FacebookUserAdapter.FacebookUserHolder;
import com.mediafever.domain.SocialUser;

/**
 * 
 * @author Maxi Rosson
 */
public class FacebookUserAdapter extends BaseHolderArrayAdapter<SocialUser, FacebookUserHolder> {
	
	public FacebookUserAdapter(Activity context, List<SocialUser> items) {
		super(context, items, R.layout.facebook_user_item);
	}
	
	@Override
	protected void fillHolderFromItem(SocialUser user, FacebookUserHolder holder) {
		holder.image.setImageContent(user.getImage(), R.drawable.user_default);
		holder.fullName.setText(user.getFullname());
		holder.socialNetworkLogo.setImageResource(R.drawable.facebook_icon);
	}
	
	@Override
	protected FacebookUserHolder createViewHolderFromConvertView(View convertView) {
		FacebookUserHolder holder = new FacebookUserHolder();
		holder.image = findView(convertView, R.id.image);
		holder.fullName = findView(convertView, R.id.fullName);
		holder.socialNetworkLogo = findView(convertView, R.id.socialNetworkLogo);
		return holder;
	}
	
	public static class FacebookUserHolder {
		
		protected CustomImageView image;
		protected TextView fullName;
		protected ImageView socialNetworkLogo;
	}
	
}