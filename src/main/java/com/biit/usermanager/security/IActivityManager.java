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
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.OrganizationDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

import java.util.Set;

public interface IActivityManager<UserId, GroupId, RoleId> {

    /**
     * Get the activities associated to this role.
     *
     * @param role
     * @return
     */
    Set<IActivity> getRoleActivities(IRole<RoleId> role) throws InvalidCredentialsException;



    /**
     * A user is authorized to perform this activity in the application.
     *
     * @param user
     * @param activity
     * @return
     * @throws UserManagementException
     */
    boolean isAuthorizedActivity(IUser<UserId> user, IActivity activity) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    /**
     * A user is authorized to perform an activity in this group.
     *
     * @param user
     * @param organization
     * @param activity
     * @return
     * @throws UserManagementException
     */
    boolean isAuthorizedActivity(IUser<UserId> user, IGroup<GroupId> organization, IActivity activity) throws UserManagementException, UserDoesNotExistException,
            OrganizationDoesNotExistException, InvalidCredentialsException;



    IRoleActivities getRoleActivities() throws InvalidCredentialsException;

    void setRoleActivities(IRoleActivities roleActivities) throws InvalidCredentialsException;



}
