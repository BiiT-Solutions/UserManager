package com.biit.usermanager.security;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface IAuthenticationService<UserId, GroupId> {

    IUser<UserId> authenticate(String userMail, String password) throws UserManagementException, AuthenticationRequired, InvalidCredentialsException,
            UserDoesNotExistException;

    IGroup<GroupId> getDefaultGroup(IUser<UserId> user) throws UserManagementException;

    IUser<UserId> getUserByEmail(String userEmail) throws UserManagementException, UserDoesNotExistException;

    IUser<UserId> getUserById(long userId) throws UserManagementException, UserDoesNotExistException;

    boolean isInGroup(IGroup<GroupId> group, IUser<UserId> user) throws UserManagementException;

    IUser<UserId> updatePassword(IUser<UserId> user, String plainTextPassword) throws UserDoesNotExistException, InvalidCredentialsException, UserManagementException;

    IUser<Long> updateUser(IUser<Long> user) throws UserManagementException;

    void reset();

    IUser<UserId> addUser(IGroup<GroupId> company, String password, String screenName, String emailAddress, String locale, String firstName, String middleName,
                          String lastName) throws UserManagementException;

    void deleteUser(IUser<UserId> user) throws UserManagementException;

    /**
     * If Spring is not available, create the required services. Be carefull, if
     * created on this way, each service uses its own pool and is not shared
     * with other different beans.
     */
    void createBeans();

}
