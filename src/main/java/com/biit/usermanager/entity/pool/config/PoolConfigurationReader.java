package com.biit.usermanager.entity.pool.config;

import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;

public class PoolConfigurationReader extends ConfigurationReader {

	private static final String CONFIG_FILE = "settings.conf";
	private static final String SYSTEM_VARIABLE_CONFIG = "USER_MANAGER_CONFIG";

	// Tags
	private static final String EXPIRATION_TIME = "usermanager.pool.expiration";

	// Default
	private static final String DEFAULT_EXPIRATION_TIME = "300000";

	private static PoolConfigurationReader instance;

	private PoolConfigurationReader() {
		super();

		addProperty(EXPIRATION_TIME, DEFAULT_EXPIRATION_TIME);

		addPropertiesSource(new PropertiesSourceFile(CONFIG_FILE));
		addPropertiesSource(new SystemVariablePropertiesSourceFile(SYSTEM_VARIABLE_CONFIG, CONFIG_FILE));

		readConfigurations();
	}

	public static PoolConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (PoolConfigurationReader.class) {
				if (instance == null) {
					instance = new PoolConfigurationReader();
				}
			}
		}
		return instance;
	}

	private String getPropertyLogException(String propertyId) {
		try {
			return getProperty(propertyId);
		} catch (PropertyNotFoundException e) {
			return null;
		}
	}

	public Long getStandardExpirationTime() {
		try {
			return Long.parseLong(getPropertyLogException(EXPIRATION_TIME));
		} catch (Exception e) {
			return Long.parseLong(DEFAULT_EXPIRATION_TIME);
		}
	}
}
