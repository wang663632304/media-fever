package com.mediafever.core.service.tvdb.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import com.jdroid.java.exception.UnexpectedException;
import com.jdroid.java.parser.Parser;

/**
 * Parser used to handle a file contained inside a zip.
 * 
 * @author Maxi Rosson
 */
public class ZipFileParser implements Parser {
	
	private Parser innerParser;
	private String fileName;
	
	/**
	 * @param innerParser {@link Parser} to use to handle the extracted file.
	 * @param fileName Name of the file to extract of the zip.
	 */
	public ZipFileParser(Parser innerParser, String fileName) {
		this.innerParser = innerParser;
		this.fileName = fileName;
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
				if (entry.getName().equals(fileName)) {
					return innerParser.parse(zipInputStream);
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
