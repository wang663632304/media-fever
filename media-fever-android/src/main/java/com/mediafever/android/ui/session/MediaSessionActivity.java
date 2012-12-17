package com.mediafever.android.ui.session;

import java.util.List;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.wizard.WizardActivity;
import com.jdroid.android.wizard.WizardStep;
import com.jdroid.java.collections.Lists;
import com.mediafever.usecase.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionActivity extends WizardActivity {
	
	private List<WizardStep> steps;
	
	/**
	 * @see com.jdroid.android.wizard.WizardActivity#getWizardSteps()
	 */
	@Override
	public List<? extends WizardStep> getWizardSteps() {
		if (steps == null) {
			steps = Lists.newArrayList();
			steps.add(new WizardStep() {
				
				@Override
				public Fragment createFragment(Object args) {
					return new MediaSessionSetupFragment();
				}
			});
			steps.add(new WizardStep() {
				
				@Override
				public Fragment createFragment(Object args) {
					return AndroidUtils.isLargeScreenOrBigger()
							&& (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.HONEYCOMB) ? new MediaSessionFriendsGridFragment()
							: new MediaSessionFriendsFragment();
				}
			});
			steps.add(new WizardStep() {
				
				@Override
				public Fragment createFragment(Object args) {
					return new MediaSelectionsFragment();
				}
			});
		}
		return steps;
	}
	
	public static class MediaSessionSetupUseCaseFragment extends UseCaseFragment<MediaSessionSetupUseCase> {
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#getUseCaseClass()
		 */
		@Override
		protected Class<MediaSessionSetupUseCase> getUseCaseClass() {
			return MediaSessionSetupUseCase.class;
		}
		
		/**
		 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
		 */
		@Override
		public void onFinishUseCase() {
			executeOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					getActivity().finish();
					ActivityLauncher.launchActivity(MediaSessionListActivity.class);
				}
			});
		}
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#executeOnInit()
		 */
		@Override
		protected Boolean executeOnInit() {
			return false;
		}
	}
	
	/**
	 * @see com.jdroid.android.wizard.WizardActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadUseCaseFragment(savedInstanceState, MediaSessionSetupUseCaseFragment.class);
	}
	
	/**
	 * @see com.jdroid.android.wizard.WizardActivity#onfinishWizard()
	 */
	@Override
	protected void onfinishWizard() {
		MediaSessionSetupUseCaseFragment useCaseFragment = (MediaSessionSetupUseCaseFragment)getUseCaseUseCaseFragment(MediaSessionSetupUseCaseFragment.class);
		useCaseFragment.executeUseCase();
	}
	
	public MediaSessionSetupUseCase getMediaSessionSetupUseCase() {
		return ((MediaSessionSetupUseCaseFragment)getUseCaseUseCaseFragment(MediaSessionSetupUseCaseFragment.class)).getUseCase();
	}
}
