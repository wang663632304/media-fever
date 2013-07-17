package com.mediafever.android.ui.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jdroid.android.dialog.CustomAlertDialogFragment;
import com.jdroid.android.utils.ToastUtils;
import com.mediafever.R;
import com.mediafever.usecase.friends.CreateFriendRequestUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class CreateFriendRequestDialogFragment extends CustomAlertDialogFragment {
	
	private static final String USER_ID_EXTRA = "userIdExtra";
	private static final String FULLNAME_EXTRA = "fullnameExtra";
	
	private CreateFriendRequestUseCase createFriendRequestUseCase;
	
	public static void show(FragmentActivity activity, Long userId, String fullname) {
		
		Fragment dialogFragment = activity.getSupportFragmentManager().findFragmentByTag(
			CreateFriendRequestDialogFragment.class.getSimpleName());
		
		if (dialogFragment == null) {
			CreateFriendRequestDialogFragment fragment = new CreateFriendRequestDialogFragment();
			fragment.addParameter(USER_ID_EXTRA, userId);
			fragment.addParameter(FULLNAME_EXTRA, fullname);
			String title = activity.getString(R.string.friendRequest);
			String yesButton = activity.getString(R.string.yes);
			String noButton = activity.getString(R.string.no);
			String message = activity.getString(R.string.addUser, fullname);
			CustomAlertDialogFragment.show(activity, fragment, title, message, yesButton, noButton, true);
		}
	}
	
	/**
	 * @see com.jdroid.android.dialog.AlertDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createFriendRequestUseCase = getInstance(CreateFriendRequestUseCase.class);
	}
	
	/**
	 * @see com.jdroid.android.dialog.CustomAlertDialogFragment#onPositiveClick()
	 */
	@Override
	protected void onPositiveClick() {
		Long userId = getArgument(USER_ID_EXTRA);
		createFriendRequestUseCase.setUserId(userId);
		executeUseCase(createFriendRequestUseCase);
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
				String fullname = getArgument(FULLNAME_EXTRA);
				if (createFriendRequestUseCase.wasAddAsFriend()) {
					ToastUtils.showInfoToast(getString(R.string.addedAsFriend, fullname));
				} else {
					ToastUtils.showInfoToast(getString(R.string.invitedToBeYourFriend, fullname));
				}
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
		onResumeUseCase(createFriendRequestUseCase, this);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(createFriendRequestUseCase, this);
	}
	
}
