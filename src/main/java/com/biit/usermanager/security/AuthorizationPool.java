package com.biit.usermanager.security;

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

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.utils.annotations.FindBugsSuppressWarnings;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Defines if an activity is authorized by an user or not.
 */
public class AuthorizationPool<UserId, OrganizationId> {

    private static final long EXPIRATION_TIME = 300000; // 300 seconds

    // user id -> time.
    private Map<IUser<UserId>, Long> time;
    // Form, user id -> activity -> allowed.
    private Map<IUser<UserId>, Map<IActivity, Boolean>> activitiesByUser;
    private Map<IUser<UserId>, Map<IGroup<OrganizationId>, Map<IActivity, Boolean>>> activitiesByOrganization;

    public AuthorizationPool() {
        reset();
    }

    public void addUser(IUser<UserId> user, IActivity activity, Boolean authorized) {
        if (user != null && activity != null && authorized != null) {
            activitiesByUser.computeIfAbsent(user, k -> new Hashtable<>());

            time.put(user, System.currentTimeMillis());
            activitiesByUser.get(user).put(activity, authorized);
        }
    }

    public void addUser(IUser<UserId> user, IGroup<OrganizationId> organization, IActivity activity,
                        Boolean authorized) {
        if (user != null && organization != null && activity != null && authorized != null) {
            activitiesByOrganization.computeIfAbsent(user, k -> new HashMap<>());

            activitiesByOrganization.get(user).computeIfAbsent(organization, k -> new HashMap<>());

            activitiesByOrganization.get(user).get(organization).put(activity, authorized);
            time.put(user, System.currentTimeMillis());
        }
    }

    /**
     * Returns true or false if the activity is authorized and null if is not catched.
     *
     * @param user
     * @param activity
     * @return
     */
    @FindBugsSuppressWarnings("NP_BOOLEAN_RETURN_NULL")
    public Boolean isAuthorizedActivity(IUser<UserId> user, IActivity activity) {
        long now = System.currentTimeMillis();
        IUser<UserId> userForm;

        if (time.size() > 0) {
            for (IUser<UserId> userIdIUser : new HashMap<>(time).keySet()) {
                userForm = userIdIUser;
                try {
                    if (time.get(userForm) != null && (now - time.get(userForm)) > EXPIRATION_TIME) {
                        // object has expired
                        removeUser(userForm);
                    } else if (user != null && user.equals(userForm)) {
                        if (activitiesByUser.get(user) != null && activity != null) {
                            return activitiesByUser.get(user).get(activity);
                        }
                    }
                } catch (Exception except) {
                    // Something is wrong. Considered as not cached.
                }
            }
        }
        return null;
    }

    @FindBugsSuppressWarnings("NP_BOOLEAN_RETURN_NULL")
    public Boolean isAuthorizedActivity(IUser<UserId> user, IGroup<OrganizationId> organization,
                                        IActivity activity) {
        long now = System.currentTimeMillis();
        IUser<UserId> authorizedUser;

        if (time.size() > 0) {
            for (IUser<UserId> userIdIUser : new HashMap<>(time).keySet()) {
                authorizedUser = userIdIUser;
                try {
                    if (time.get(authorizedUser) != null && (now - time.get(authorizedUser)) > EXPIRATION_TIME) {
                        // object has expired
                        removeUser(authorizedUser);
                    } else if (user != null && user.equals(authorizedUser)) {
                        if (activitiesByOrganization.get(user) != null && activitiesByOrganization.get(user).get(organization) != null
                                && activity != null) {
                            return activitiesByOrganization.get(user).get(organization).get(activity);
                        }
                    }
                } catch (Exception except) {
                    // Something is wrong. Considered as not cached.
                }
            }
        }
        return null;
    }

    public void removeUser(IUser<UserId> user) {
        if (user != null) {
            time.remove(user);
            activitiesByUser.remove(user);
            activitiesByOrganization.remove(user);
        }
    }

    public void reset() {
        time = new HashMap<>();
        activitiesByUser = new HashMap<>();
        activitiesByOrganization = new HashMap<>();
    }
}
