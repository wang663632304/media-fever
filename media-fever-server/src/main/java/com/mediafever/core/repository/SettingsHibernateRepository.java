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
	public Settings getSeriesLastUpdateSetting() {
		return findUnique("settingsKey", SettingsKey.TV_DB_LAST_UPDATE);
	}
	
	/**
	 * @see com.mediafever.core.repository.SettingsRepository#getMovieImageBaseURL()
	 */
	@Override
	public String getMovieImageBaseURL() {
		return findUnique("settingsKey", SettingsKey.MOVIE_DB_IMAGE_URL).getValue();
	}
	
	/**
	 * @see com.mediafever.core.repository.SettingsRepository#getMovieTrailerBaseURL()
	 */
	@Override
	public String getMovieTrailerBaseURL() {
		return findUnique("settingsKey", SettingsKey.MOVIE_DB_TRAILER_URL).getValue();
	}
}
