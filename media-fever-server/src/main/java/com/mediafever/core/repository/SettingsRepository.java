package com.mediafever.core.repository;

import com.jdroid.java.repository.Repository;
import com.mediafever.core.domain.watchable.Settings;

/**
 * Repository to handle application settings.
 * 
 * @author Estefanía Caravatti
 */
public interface SettingsRepository extends Repository<Settings> {
	
	/**
	 * @return The last time Series information was synchronized.
	 */
	public Settings getSeriesLastUpdate();
}
