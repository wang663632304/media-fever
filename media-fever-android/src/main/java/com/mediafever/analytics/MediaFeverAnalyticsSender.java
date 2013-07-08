package com.mediafever.analytics;

import com.jdroid.android.analytics.AnalyticsSender;
import com.jdroid.android.analytics.AnalyticsTracker;
import com.jdroid.android.analytics.GoogleAnalyticsTracker;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaFeverAnalyticsSender extends AnalyticsSender<AnalyticsTracker> {
	
	private static final MediaFeverAnalyticsSender INSTANCE = new MediaFeverAnalyticsSender(
			GoogleAnalyticsTracker.get());
	
	public static MediaFeverAnalyticsSender get() {
		return INSTANCE;
	}
	
	public MediaFeverAnalyticsSender(AnalyticsTracker... trackers) {
		super(trackers);
	}
}
