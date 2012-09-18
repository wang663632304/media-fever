package com.mediafever.context;

import android.preference.PreferenceManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.java.utils.PropertiesUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class ApplicationContext extends DefaultApplicationContext {
	
	private static final ApplicationContext INSTANCE = new ApplicationContext();
	
	/**
	 * @return The {@link ApplicationContext} instance
	 */
	public static ApplicationContext get() {
		return INSTANCE;
	}
	
	private String contactUsEmail;
	private String serverApiURL;
	private String serverTokenSecret;
	
	public ApplicationContext() {
		contactUsEmail = PropertiesUtils.getStringProperty("mail.contact");
		serverApiURL = PropertiesUtils.getStringProperty("server.url");
		serverTokenSecret = PropertiesUtils.getStringProperty("server.token.secret");
	}
	
	public Boolean isHttpMockEnabled() {
		return !isProductionEnvironment()
				&& PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean(
					"httpMockEnabled", false);
	}
	
	public Integer getHttpMockSleepDuration() {
		return PreferenceManager.getDefaultSharedPreferences(AbstractApplication.get()).getBoolean("httpMockSleep",
			false) ? 10 : null;
	}
	
	/**
	 * @return The base URL of the API server
	 */
	public String getServerApiUrl() {
		return serverApiURL;
	}
	
	/**
	 * @return The token's secret used by the API server
	 */
	public String getServerTokenSecret() {
		return serverTokenSecret;
	}
	
	public String getContactUsEmail() {
		return contactUsEmail;
	}
	
}
