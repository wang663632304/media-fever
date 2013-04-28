package com.mediafever.android.ui;

import com.jdroid.android.dialog.AbstractAboutDialogFragment;
import com.mediafever.context.ApplicationContext;

/**
 * 
 * @author Maxi Rosson
 */
public class AboutDialogFragment extends AbstractAboutDialogFragment {
	
	/**
	 * @see com.jdroid.android.dialog.AbstractAboutDialogFragment#getContactUsEmail()
	 */
	@Override
	protected String getContactUsEmail() {
		return ApplicationContext.get().getContactUsEmail();
	}
}
