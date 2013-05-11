package com.mediafever.core.domain.watchable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.jdroid.javaweb.domain.Entity;

/**
 * Class to handle application settings as a key-value pair.
 * 
 * @author Estefanía Caravatti
 */
@javax.persistence.Entity
public class Settings extends Entity {
	
	@Enumerated(value = EnumType.STRING)
	private SettingsKey settingsKey;
	
	private String value;
	
	@SuppressWarnings("unused")
	private Settings() {
		// Required by Hibernate.
	}
	
	public Settings(SettingsKey settingsKey, String value) {
		this.settingsKey = settingsKey;
		this.value = value;
	}
	
	/**
	 * Updates the settings value.
	 * 
	 * @param value The new value.
	 */
	public void update(String value) {
		this.value = value;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Keys allowed in {@link Settings}.
	 * 
	 * @author Estefanía Caravatti
	 */
	public enum SettingsKey {
		TV_DB_LAST_UPDATE // The last time an update was made from the TvDb.
	}
	
}
