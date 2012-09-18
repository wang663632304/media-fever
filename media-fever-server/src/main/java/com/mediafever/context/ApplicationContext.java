package com.mediafever.context;

import com.jdroid.javaweb.context.AbstractApplicationContext;
import com.mediafever.core.domain.User;

/**
 * The application context
 */
public class ApplicationContext extends AbstractApplicationContext<User> {
	
	private static final ApplicationContext INSTANCE = new ApplicationContext();
	
	private String apiVersion;
	private String tokenSecret;
	private String moviesApiURL;
	private String moviesApiKey;
	private String tvApiURL;
	private String tvApiKey;
	
	private ApplicationContext() {
		// Do nothing
	}
	
	/**
	 * @return The {@link ApplicationContext} instance
	 */
	public static ApplicationContext get() {
		return ApplicationContext.INSTANCE;
	}
	
	/**
	 * @return the apiVersion
	 */
	public String getApiVersion() {
		return apiVersion;
	}
	
	/**
	 * @param apiVersion the apiVersion to set
	 */
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
	/**
	 * @return the tokenSecret
	 */
	public String getTokenSecret() {
		return tokenSecret;
	}
	
	/**
	 * @param tokenSecret the tokenSecret to set
	 */
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	
	public String getMoviesApiURL() {
		return moviesApiURL;
	}
	
	public void setMoviesApiURL(String moviesApiURL) {
		this.moviesApiURL = moviesApiURL;
	}
	
	public String getMoviesApiKey() {
		return moviesApiKey;
	}
	
	public void setMoviesApiKey(String moviesApiKey) {
		this.moviesApiKey = moviesApiKey;
	}
	
	public String getTvApiURL() {
		return tvApiURL;
	}
	
	public void setTvApiURL(String tvApiURL) {
		this.tvApiURL = tvApiURL;
	}
	
	public String getTvApiKey() {
		return tvApiKey;
	}
	
	public void setTvApiKey(String tvApiKey) {
		this.tvApiKey = tvApiKey;
	}
}
