package com.mediafever.core.service.tvdb.parser;

import org.xml.sax.Attributes;
import com.jdroid.java.parser.xml.XmlParser;
import com.jdroid.java.utils.NumberUtils;

/**
 * Parser that handles series initial update information.
 * 
 * @author Estefanía Caravatti
 */
public class SeriesInitialUpdateParser extends XmlParser {
	
	private static final String SERIES_TAG = "Series";
	private static final String TIME_TAG = "time";
	private static final String ID_TAG = "id";
	private static final String BANNER_TAG = "Banner";
	
	private SeriesUpdateResponse response = new SeriesUpdateResponse();
	
	private Boolean parsingSeries = Boolean.FALSE;
	private Boolean ignoreTag = Boolean.FALSE;
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#onStartElement(java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	protected void onStartElement(String localName, Attributes attributes) {
		if (!ignoreTag) {
			if (SERIES_TAG.equals(localName)) {
				parsingSeries = Boolean.TRUE;
			}
		} else if (BANNER_TAG.equals(localName)) {
			ignoreTag = Boolean.TRUE;
		}
	}
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#onEndElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected void onEndElement(String localName, String content) {
		if (!ignoreTag) {
			if (parsingSeries) {
				if (ID_TAG.equals(localName)) {
					response.addSeries(NumberUtils.getLong(content));
				} else if (SERIES_TAG.equals(localName)) {
					parsingSeries = Boolean.FALSE;
				} else if (TIME_TAG.equals(localName)) {
					response.setTime(content);
				}
			}
		} else if (BANNER_TAG.equals(localName)) {
			ignoreTag = Boolean.FALSE;
		}
	}
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#getResponse()
	 */
	@Override
	protected Object getResponse() {
		return response;
	}
	
}
