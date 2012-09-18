package com.mediafever.parser;

import java.util.List;
import org.json.JSONException;
import com.jdroid.android.search.PagedResult;
import com.jdroid.java.parser.json.JsonArrayParser;
import com.jdroid.java.parser.json.JsonObjectWrapper;
import com.jdroid.java.parser.json.JsonParser;

/**
 * 
 * @author Maxi Rosson
 */
public class PagedResultParser extends JsonParser<JsonObjectWrapper> {
	
	private static final String LAST_PAGE = "lastPage";
	private static final String LIST = "list";
	
	private JsonArrayParser parser;
	
	public PagedResultParser(JsonParser<JsonObjectWrapper> parser) {
		this.parser = new JsonArrayParser(parser);
	}
	
	/**
	 * @see com.jdroid.java.parser.json.JsonArrayParser#parse(com.jdroid.java.parser.json.JsonArrayWrapper)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object parse(JsonObjectWrapper json) throws JSONException {
		Boolean lastPage = json.optBoolean(LAST_PAGE);
		if (lastPage != null) {
			List<?> list = (List<?>)parser.parse(json.getJSONArray(LIST));
			return new PagedResult(lastPage, list);
		} else {
			return new PagedResult();
		}
	}
	
}
