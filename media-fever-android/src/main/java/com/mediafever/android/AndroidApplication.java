package com.mediafever.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.android.gcm.GCMRegistrar;
import com.google.inject.AbstractModule;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.activity.BaseActivity;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.exception.ExceptionHandler;
import com.jdroid.android.fragment.BaseFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.NotificationUtils;
import com.mediafever.android.exception.AndroidExceptionHandler;
import com.mediafever.android.service.DisableDeviceService;
import com.mediafever.android.service.EnableDeviceService;
import com.mediafever.android.ui.home.HomeActivity;
import com.mediafever.android.ui.login.LoginActivity;
import com.mediafever.context.ApplicationContext;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidApplication extends AbstractApplication {
	
	private static final String TAG = AndroidApplication.class.getSimpleName();
	
	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		if (SecurityContext.get().isAuthenticated()) {
			if (GCMRegistrar.isRegistered(this)) {
				Log.d(TAG, "GCM already registered on the device");
				if (!GCMRegistrar.isRegisteredOnServer(this)) {
					EnableDeviceService.runIntentInService(this);
				}
			} else {
				GCMRegistrar.register(this, getAndroidApplicationContext().getGoogleProjectId());
			}
		}
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createApplicationContext()
	 */
	@Override
	protected DefaultApplicationContext createApplicationContext() {
		return ApplicationContext.get();
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createExceptionHandler()
	 */
	@Override
	protected ExceptionHandler createExceptionHandler() {
		return new AndroidExceptionHandler();
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createAndroidModule()
	 */
	@Override
	protected AbstractModule createAndroidModule() {
		return new AndroidModule();
	}
	
	@TargetApi(11)
	public void logout() {
		
		DisableDeviceService.setUserToken(SecurityContext.get().getUser().getUserToken());
		GCMRegistrar.unregister(this);
		
		SecurityContext.get().detachUser();
		NotificationUtils.cancelAllNotifications();
		
		ActivityLauncher.launchActivityClearTask(LoginActivity.class, true);
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#getHomeActivityClass()
	 */
	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return HomeActivity.class;
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createBaseActivity(android.app.Activity)
	 */
	@Override
	public BaseActivity createBaseActivity(Activity activity) {
		return new AndroidBaseActivity(activity);
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#createBaseFragment(android.support.v4.app.Fragment)
	 */
	@Override
	public BaseFragment createBaseFragment(Fragment fragment) {
		return new AndroidBaseFragment(fragment);
	}
	
	/**
	 * @see com.jdroid.android.AbstractApplication#isGcmEnabled()
	 */
	@Override
	public Boolean isGcmEnabled() {
		return true;
	}
	
	public Boolean isLeftNavBarEnabled() {
		return AndroidUtils.isGoogleTV() || (AndroidUtils.isXLargeScreenOrBigger() && !AndroidUtils.isPreHoneycomb());
	}
}
