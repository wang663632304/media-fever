package com.mediafever.android.ui;

import java.util.List;

/**
 * 
 * @param <T>
 * @author Maxi Rosson
 */
public interface Reloadable<T> {
	
	public void reload(List<T> items);
	
}
