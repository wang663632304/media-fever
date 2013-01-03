package com.mediafever.android.ui.session;

import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.jdroid.android.fragment.BaseFragment.UseCaseTrigger;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.wizard.WizardActivity;
import com.jdroid.android.wizard.WizardStep;
import com.jdroid.java.collections.Lists;
import com.mediafever.usecase.mediasession.MediaSessionDetailsUseCase;
import com.mediafever.usecase.mediasession.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSessionActivity extends WizardActivity {
	
	public final static String MEDIA_SESSION_ID_EXTRA = "mediaSessionIdExtra";
	
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
					return AndroidUtils.isLargeScreenOrBigger() && !AndroidUtils.isPreHoneycomb() ? new MediaSessionFriendsGridFragment()
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
	
	public static class MediaSessionDetailsUseCaseFragment extends UseCaseFragment<MediaSessionDetailsUseCase> {
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#getUseCaseClass()
		 */
		@Override
		protected Class<MediaSessionDetailsUseCase> getUseCaseClass() {
			return MediaSessionDetailsUseCase.class;
		}
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#intializeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase)
		 */
		@Override
		protected void intializeUseCase(MediaSessionDetailsUseCase useCase) {
			useCase.setMediaSessionId(getActivity().getIntent().getLongExtra(MEDIA_SESSION_ID_EXTRA, 0L));
		}
		
		/**
		 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
		 */
		@Override
		public void onFinishUseCase() {
			executeOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					MediaSessionActivity activity = ((MediaSessionActivity)getActivity());
					activity.getMediaSessionSetupUseCase().setMediaSession(getUseCase().getMediaSession());
					activity.loadWizard();
				}
			});
		}
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
					Intent intent = new Intent(getActivity(), MediaSessionListActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
				}
			});
		}
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#getuseCaseTrigger()
		 */
		@Override
		protected UseCaseTrigger getuseCaseTrigger() {
			return UseCaseTrigger.MANUAL;
		}
	}
	
	/**
	 * @see com.jdroid.android.wizard.WizardActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadUseCaseFragment(savedInstanceState, MediaSessionSetupUseCaseFragment.class);
		getSupportFragmentManager().executePendingTransactions();
		if (getIntent().hasExtra(MEDIA_SESSION_ID_EXTRA)
				&& (getMediaSessionSetupUseCase().getMediaSession().getId() == null)) {
			loadUseCaseFragment(savedInstanceState, MediaSessionDetailsUseCaseFragment.class);
		} else {
			loadWizard();
		}
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
