package com.mediafever.android.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.images.CustomImageView;
import com.jdroid.android.images.ImageHolder;
import com.mediafever.R;

/**
 * 
 * @author Maxi Rosson
 */
public class BorderImage extends RelativeLayout implements ImageHolder {
	
	private CustomImageView customImageView;
	
	public BorderImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public BorderImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BorderImage(Context context) {
		super(context);
		init();
	}
	
	public BorderImage(Context context, int widthResId, int heightResId) {
		super(context);
		LayoutParams lp = new LayoutParams(getResources().getDimensionPixelSize(widthResId),
				getResources().getDimensionPixelSize(heightResId));
		setLayoutParams(lp);
		init();
	}
	
	private void init() {
		
		View view = new View(getContext());
		view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		view.setBackgroundColor(Color.WHITE);
		addView(view);
		
		customImageView = new CustomImageView(getContext());
		LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		int margin = getResources().getDimensionPixelSize(R.dimen.imageLayoutMargin);
		lp.setMargins(margin, margin, margin, margin);
		customImageView.setLayoutParams(lp);
		customImageView.setImageResource(R.drawable.user_default);
		customImageView.setScaleType(ScaleType.CENTER_CROP);
		customImageView.setId(R.id.image);
		addView(customImageView);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageBitmap(android.graphics.Bitmap)
	 */
	@Override
	public void setImageBitmap(Bitmap bitmap) {
		customImageView.setImageBitmap(bitmap);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#showStubImage()
	 */
	@Override
	public void showStubImage() {
		customImageView.showStubImage();
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumWidth()
	 */
	@Override
	public Integer getMaximumWidth() {
		return customImageView.getMaximumWidth();
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#getMaximumHeight()
	 */
	@Override
	public Integer getMaximumHeight() {
		return customImageView.getMaximumHeight();
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId) {
		customImageView.setImageContent(imageUri, stubId);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int,
	 *      java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId, Integer maxWidth, Integer maxHeight) {
		customImageView.setImageContent(fileContent, stubId, maxWidth, maxHeight);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(com.jdroid.android.domain.FileContent, int)
	 */
	@Override
	public void setImageContent(FileContent fileContent, int stubId) {
		customImageView.setImageContent(fileContent, stubId);
	}
	
	/**
	 * @see com.jdroid.android.images.ImageHolder#setImageContent(android.net.Uri, int, java.lang.Integer,
	 *      java.lang.Integer)
	 */
	@Override
	public void setImageContent(Uri imageUri, int stubId, Integer maxWidth, Integer maxHeight) {
		customImageView.setImageContent(imageUri, stubId, maxWidth, maxHeight);
	}
}
