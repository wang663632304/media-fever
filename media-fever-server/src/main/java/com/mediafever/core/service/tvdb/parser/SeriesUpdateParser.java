package com.mediafever.core.service.tvdb.parser;

import org.xml.sax.Attributes;
import com.jdroid.java.parser.xml.XmlParser;
import com.jdroid.java.utils.NumberUtils;

/**
 * Parser that handles series update information.
 * 
 * @author Estefanía Caravatti
 */
public class SeriesUpdateParser extends XmlParser {
	
	private static final String SERIES_TAG = "Series";
	private static final String TIME_TAG = "Time";
	
	private SeriesUpdateResponse response = new SeriesUpdateResponse();
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#onStartElement(java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	protected void onStartElement(String localName, Attributes attributes) {
		// Nothing to do here.
	}
	
	/**
	 * @see com.jdroid.java.parser.xml.XmlParser#onEndElement(java.lang.String, java.lang.String)
	 */
	@Override
	protected void onEndElement(String localName, String content) {
		if (SERIES_TAG.equals(localName)) {
			response.addSeries(NumberUtils.getLong(content));
		} else if (TIME_TAG.equals(localName)) {
			response.setTime(content);
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
