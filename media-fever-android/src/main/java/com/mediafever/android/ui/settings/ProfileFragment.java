package com.mediafever.android.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.jdroid.android.domain.FileContent;
import com.jdroid.android.fragment.AbstractFragment;
import com.jdroid.android.images.CustomImageView;
import com.jdroid.android.picture.PictureDialogFragment;
import com.jdroid.android.picture.PicturePickerListener;
import com.jdroid.android.utils.ToastUtils;
import com.mediafever.R;
import com.mediafever.domain.UserImpl;
import com.mediafever.usecase.UpdateUserProfileUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class ProfileFragment extends AbstractFragment implements PicturePickerListener {
	
	private final static int MAX_AVATAR_HEIGHT = 120;
	private final static int MAX_AVATAR_WIDTH = 120;
	
	private UpdateUserProfileUseCase updateUserProfileUseCase;
	
	private CustomImageView avatar;
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		updateUserProfileUseCase = getInstance(UpdateUserProfileUseCase.class);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.profile_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		UserImpl user = (UserImpl)getUser();
		
		final TextView email = findView(R.id.email);
		email.setText(user.getEmail());
		
		final TextView confirmEmail = findView(R.id.confirmEmail);
		confirmEmail.setText(user.getEmail());
		
		final TextView firstName = findView(R.id.firstName);
		firstName.setText(user.getFirstName());
		
		final TextView lastName = findView(R.id.lastName);
		lastName.setText(user.getLastName());
		
		final CheckBox publicProfile = findView(R.id.publicProfile);
		publicProfile.setChecked(user.hasPublicProfile());
		
		avatar = findView(R.id.photo);
		avatar.setImageContent(user.getImage(), R.drawable.person_default, MAX_AVATAR_WIDTH, MAX_AVATAR_HEIGHT);
		if (PictureDialogFragment.display()) {
			
			avatar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PictureDialogFragment.show(ProfileFragment.this);
				}
			});
		}
		
		final CheckBox changePassword = findView(R.id.changePassword);
		final TextView confirmPassword = findView(R.id.confirmPassword);
		final View passwordContainer = findView(R.id.passwordContainer);
		final TextView password = findView(R.id.password);
		changePassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				passwordContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
				password.setText(null);
				confirmPassword.setText(null);
			}
		});
		
		findView(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateUserProfileUseCase.setFirstName(firstName.getText().toString());
				updateUserProfileUseCase.setLastName(lastName.getText().toString());
				updateUserProfileUseCase.setEmail(email.getText().toString());
				updateUserProfileUseCase.setConfirmEmail(confirmEmail.getText().toString());
				updateUserProfileUseCase.setPassword(password.getText().toString());
				updateUserProfileUseCase.setConfirmPassword(confirmPassword.getText().toString());
				updateUserProfileUseCase.setChangePassword(changePassword.isChecked());
				updateUserProfileUseCase.setPublicProfile(publicProfile.isChecked());
				executeUseCase(updateUserProfileUseCase);
			}
		});
		
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(updateUserProfileUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(updateUserProfileUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				dismissLoading();
				ToastUtils.showToast(R.string.userProfileSaved);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.picture.PicturePickerListener#onPicturePicked(com.jdroid.android.domain.FileContent)
	 */
	@Override
	public void onPicturePicked(FileContent fileContent) {
		updateUserProfileUseCase.setAvatar(fileContent);
		avatar.setImageContent(fileContent, R.drawable.person_default, MAX_AVATAR_WIDTH, MAX_AVATAR_HEIGHT);
	}
}
