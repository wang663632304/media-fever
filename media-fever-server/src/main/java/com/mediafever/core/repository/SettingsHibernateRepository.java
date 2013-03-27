package com.mediafever.core.repository;

import com.mediafever.core.domain.watchable.Settings;
import com.mediafever.core.domain.watchable.Settings.SettingsKey;

/**
 * Repository to handle {@link Settings}'s persistence.
 * 
 * @author Estefanía Caravatti
 */
public class SettingsHibernateRepository extends HibernateRepository<Settings> implements SettingsRepository {
	
	protected SettingsHibernateRepository() {
		super(Settings.class);
	}
	
	@Override
	public Settings getSeriesLastUpdate() {
		return findUnique("settingsKey", SettingsKey.TV_DB_LAST_UPDATE);
	}
}
