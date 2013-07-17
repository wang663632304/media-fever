package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.dialog.CustomAlertDialogFragment;
import com.jdroid.android.utils.ToastUtils;
import com.mediafever.R;
import com.mediafever.domain.FacebookUser;
import com.mediafever.usecase.friends.InviteFacebookFriendUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class InviteFacebookFriendDialogFragment extends CustomAlertDialogFragment {
	
	private static final String FACEBOOK_USER_EXTRA = "facebookUserExtra";
	
	private InviteFacebookFriendUseCase inviteFacebookFriendUseCase;
	
	public static void show(FragmentActivity activity, FacebookUser facebookUser) {
		
		Fragment dialogFragment = activity.getSupportFragmentManager().findFragmentByTag(
			InviteFacebookFriendDialogFragment.class.getSimpleName());
		
		if (dialogFragment == null) {
			InviteFacebookFriendDialogFragment fragment = new InviteFacebookFriendDialogFragment();
			fragment.addParameter(FACEBOOK_USER_EXTRA, facebookUser);
			String title = activity.getString(R.string.appInviteTitle);
			String yesButton = activity.getString(R.string.yes);
			String noButton = activity.getString(R.string.no);
			String message = activity.getString(R.string.appInviteMessage, facebookUser.getFullname());
			CustomAlertDialogFragment.show(activity, fragment, title, message, yesButton, noButton, true);
		}
	}
	
	private FacebookUser facebookUser;
	
	/**
	 * @see com.jdroid.android.dialog.AlertDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		facebookUser = getArgument(FACEBOOK_USER_EXTRA);
		inviteFacebookFriendUseCase = getInstance(InviteFacebookFriendUseCase.class);
	}
	
	/**
	 * @see com.jdroid.android.dialog.CustomAlertDialogFragment#onPositiveClick()
	 */
	@Override
	protected void onPositiveClick() {
		inviteFacebookFriendUseCase.setFacebookUser(facebookUser);
		executeUseCase(inviteFacebookFriendUseCase);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				dismissLoading();
				ToastUtils.showInfoToast(getString(R.string.invitedToApp, facebookUser.getFullname()));
				dismiss();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return false;
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(inviteFacebookFriendUseCase, this);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(inviteFacebookFriendUseCase, this);
	}
}
