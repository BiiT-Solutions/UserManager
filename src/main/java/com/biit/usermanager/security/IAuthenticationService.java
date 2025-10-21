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
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface IAuthenticationService<UserId, GroupId> {

    IUser<UserId> authenticate(String userMail, String password) throws UserManagementException, AuthenticationRequired, InvalidCredentialsException,
            UserDoesNotExistException;

    IGroup<GroupId> getDefaultGroup(IUser<UserId> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    IUser<UserId> getUserByEmail(String userEmail) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    IUser<UserId> getUserById(long userId) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    boolean isInGroup(IGroup<GroupId> group, IUser<UserId> user) throws UserManagementException, InvalidCredentialsException;

    IUser<UserId> updatePassword(IUser<UserId> user, String plainTextPassword)
            throws UserDoesNotExistException, InvalidCredentialsException, UserManagementException;

    IUser<Long> updateUser(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    void reset();

    IUser<UserId> addUser(IGroup<GroupId> company, String password, String screenName, String emailAddress, String locale, String firstName, String middleName,
                          String lastName) throws UserManagementException, InvalidCredentialsException;

    IUser<UserId> addUser(IUser<UserId> user) throws UserManagementException, InvalidCredentialsException;

    void deleteUser(IUser<UserId> user) throws UserManagementException, InvalidCredentialsException;

    /**
     * If Spring is not available, create the required services. Be careful, if
     * created on this way, each service uses its own pool and is not shared
     * with other different beans.
     */
    void createBeans();

}
