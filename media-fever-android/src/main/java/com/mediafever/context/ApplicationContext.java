package com.mediafever.context;

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
	private String serverTokenSecret;
	
	public ApplicationContext() {
		contactUsEmail = PropertiesUtils.getStringProperty("mail.contact");
		serverTokenSecret = PropertiesUtils.getStringProperty("server.token.secret");
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
