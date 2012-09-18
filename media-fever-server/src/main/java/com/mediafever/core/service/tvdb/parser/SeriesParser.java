package com.mediafever.core.service.tvdb.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.parser.Parser;
import com.mediafever.core.repository.PeopleRepository;

/**
 * 
 * @author Maxi Rosson
 */
public class SeriesParser implements Parser {
	
	private PeopleRepository peopleRepository;
	
	public SeriesParser(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.io.InputStream)
	 */
	@Override
	public Object parse(InputStream inputStream) {
		
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		try {
			ZipEntry entry = null;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				if (entry.getName().equals("en.xml")) {
					return new SeriesDetailsParser(peopleRepository).parse(zipInputStream);
				}
			}
		} catch (IOException e) {
			throw new UnexpectedException(e);
		}
		return null;
	}
	
	/**
	 * @see com.jdroid.java.parser.Parser#parse(java.lang.String)
	 */
	@Override
	public Object parse(String input) {
		return null;
	}
}
