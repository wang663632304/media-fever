package com.mediafever.android.ui.settings;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
	
	@InjectView(R.id.firstName)
	private TextView firstName;
	
	@InjectView(R.id.lastName)
	private TextView lastName;
	
	@InjectView(R.id.email)
	private TextView email;
	
	@InjectView(R.id.confirmEmail)
	private TextView confirmEmail;
	
	@InjectView(R.id.changePassword)
	private CheckBox changePassword;
	
	@InjectView(R.id.passwordContainer)
	private View passwordContainer;
	
	@InjectView(R.id.password)
	private TextView password;
	
	@InjectView(R.id.confirmPassword)
	private TextView confirmPassword;
	
	@InjectView(R.id.publicProfile)
	private CheckBox publicProfile;
	
	@InjectView(R.id.save)
	private Button save;
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.profile_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		if (updateUserProfileUseCase == null) {
			updateUserProfileUseCase = getInstance(UpdateUserProfileUseCase.class);
		}
		
		avatar = findView(R.id.photo);
		changePassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				passwordContainer.setVisibility(isChecked ? View.VISIBLE : View.GONE);
				password.setText(null);
				confirmPassword.setText(null);
			}
		});
		
		save.setOnClickListener(new OnClickListener() {
			
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
		
		UserImpl user = (UserImpl)getUser();
		email.setText(user.getEmail());
		confirmEmail.setText(user.getEmail());
		firstName.setText(user.getFirstName());
		lastName.setText(user.getLastName());
		publicProfile.setChecked(user.hasPublicProfile());
		avatar.setImageContent(user.getImage(), R.drawable.person_default, MAX_AVATAR_WIDTH, MAX_AVATAR_HEIGHT);
		if (PictureDialogFragment.display()) {
			
			avatar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PictureDialogFragment.show(ProfileFragment.this);
				}
			});
		}
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
