package com.mediafever.android.exception;

import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.exception.InvalidApiVersionException;
import com.jdroid.android.exception.InvalidUserTokenException;
import com.jdroid.android.utils.GooglePlayUtils;
import com.mediafever.android.AndroidApplication;

/**
 * 
 * @author Maxi Rosson
 */
public class AndroidExceptionHandler extends DefaultExceptionHandler {
	
	/**
	 * @see com.jdroid.android.exception.DefaultExceptionHandler#doUncaughtException(java.lang.Thread,
	 *      java.lang.Throwable)
	 */
	@Override
	public Boolean doUncaughtException(Thread thread, Throwable throwable) {
		if (throwable instanceof InvalidApiVersionException) {
			handleInvalidApiVersion(thread, (InvalidApiVersionException)throwable);
			return true;
		} else if (throwable instanceof InvalidUserTokenException) {
			handleInvalidUserToken(thread, (InvalidUserTokenException)throwable);
			return true;
		}
		return false;
	}
	
	private void handleInvalidApiVersion(Thread thread, InvalidApiVersionException exception) {
		Runnable beforeLoginRunnable = new Runnable() {
			
			@Override
			public void run() {
				GooglePlayUtils.showUpdateDialog();
			}
		};
		
		if (SecurityContext.get().isAuthenticated()) {
			AbstractApplication.get().setLoginRunnable(beforeLoginRunnable);
			AndroidApplication.get().logout();
		} else {
			// If an InvalidApiVersionException is thrown when the user is not logged, we don't need to logout it.
			// We trigger the Update Dialog directly from here
			beforeLoginRunnable.run();
		}
	}
	
	private void handleInvalidUserToken(Thread thread, InvalidUserTokenException exception) {
		AndroidApplication.get().logout();
	}
}
