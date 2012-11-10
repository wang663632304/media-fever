package com.mediafever.android.ui.session;

import java.util.List;
import android.support.v4.app.Fragment;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.wizard.WizardActivity;
import com.jdroid.android.wizard.WizardStep;
import com.jdroid.java.collections.Lists;

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
					return new MediaSessionFriendsFragment();
				}
			});
			steps.add(new WizardStep() {
				
				@Override
				public Fragment createFragment(Object args) {
					return new MediaSessionWatchablesFragment();
				}
			});
		}
		return steps;
	}
	
	/**
	 * @see com.jdroid.android.wizard.WizardActivity#onfinishWizard()
	 */
	@Override
	protected void onfinishWizard() {
		// TODO Auto-generated method stub
		ActivityLauncher.launchHomeActivity();
	}
}
