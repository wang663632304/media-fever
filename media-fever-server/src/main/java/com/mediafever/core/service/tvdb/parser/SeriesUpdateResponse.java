package com.mediafever.core.service.tvdb.parser;

import java.util.List;
import com.google.common.collect.Lists;

public class SeriesUpdateResponse {
	
	private List<Long> seriesIds = Lists.newArrayList();
	private String time;
	
	public void addSeries(Long seriesId) {
		seriesIds.add(seriesId);
	}
	
	public void setTime(String time) {
		this.time = ((this.time != null) && (this.time.compareTo(time) > 0)) ? this.time : time;
	}
	
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	
	/**
	 * @return the seriesIds
	 */
	public List<Long> getSeriesIds() {
		return seriesIds;
	}
}