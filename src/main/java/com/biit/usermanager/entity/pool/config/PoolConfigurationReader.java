package com.biit.usermanager.entity.pool.config;

/*-
 * #%L
 * User Manager Common Utils
 * %%
 * Copyright (C) 2015 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.utils.configuration.ConfigurationReader;
import com.biit.utils.configuration.PropertiesSourceFile;
import com.biit.utils.configuration.SystemVariablePropertiesSourceFile;
import com.biit.utils.configuration.exceptions.PropertyNotFoundException;

public final class PoolConfigurationReader extends ConfigurationReader {

    private static final String CONFIG_FILE = "settings.conf";
    private static final String SYSTEM_VARIABLE_CONFIG = "USER_MANAGER_CONFIG";

    // Tags
    private static final String EXPIRATION_TIME = "usermanager.pool.expiration";
    private static final String USER_POOL_EXPIRATION_TIME = "usermanager.user.pool.expiration";
    private static final String GROUP_POOL_EXPIRATION_TIME = "usermanager.group.pool.expiration";
    private static final String ROLE_POOL_EXPIRATION_TIME = "usermanager.role.pool.expiration";
    private static final String ACTIVITY_POOL_EXPIRATION_TIME = "usermanager.activity.pool.expiration";

    // Default
    private static final String DEFAULT_EXPIRATION_TIME = "300000";

    private static PoolConfigurationReader instance;

    private PoolConfigurationReader() {
        super();

        addProperty(EXPIRATION_TIME, DEFAULT_EXPIRATION_TIME);
        addProperty(USER_POOL_EXPIRATION_TIME, DEFAULT_EXPIRATION_TIME);
        addProperty(GROUP_POOL_EXPIRATION_TIME, DEFAULT_EXPIRATION_TIME);
        addProperty(ROLE_POOL_EXPIRATION_TIME, DEFAULT_EXPIRATION_TIME);
        addProperty(ACTIVITY_POOL_EXPIRATION_TIME, DEFAULT_EXPIRATION_TIME);

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

    public Long getUserPoolExpirationTime() {
        try {
            return Long.parseLong(getPropertyLogException(USER_POOL_EXPIRATION_TIME));
        } catch (Exception e) {
            return getStandardExpirationTime();
        }
    }

    public Long getGroupPoolExpirationTime() {
        try {
            return Long.parseLong(getPropertyLogException(GROUP_POOL_EXPIRATION_TIME));
        } catch (Exception e) {
            return getStandardExpirationTime();
        }
    }

    public Long getRolePoolExpirationTime() {
        try {
            return Long.parseLong(getPropertyLogException(ROLE_POOL_EXPIRATION_TIME));
        } catch (Exception e) {
            return getStandardExpirationTime();
        }
    }

    public Long getActivityPoolExpirationTime() {
        try {
            return Long.parseLong(getPropertyLogException(ACTIVITY_POOL_EXPIRATION_TIME));
        } catch (Exception e) {
            return getStandardExpirationTime();
        }
    }

}
