package com.biit.usermanager.security;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface IAuthenticationService<UserId, GroupId> {

	IUser<UserId> authenticate(String userMail, String password)
			throws UserManagementException, AuthenticationRequired, InvalidCredentialsException;

	IGroup<GroupId> getDefaultGroup(IUser<UserId> user) throws UserManagementException;

	IUser<UserId> getUserByEmail(String userEmail) throws UserManagementException;

	IUser<UserId> getUserById(long userId) throws UserManagementException;

	boolean isInGroup(IGroup<GroupId> group, IUser<UserId> user) throws UserManagementException;

	IUser<UserId> updatePassword(IUser<UserId> user, String plainTextPassword) throws UserManagementException;

	IUser<Long> updateUser(IUser<Long> user) throws UserManagementException;

	void reset();

	IUser<UserId> addUser(IGroup<GroupId> company, String password, String screenName, String emailAddress, String locale,
			String firstName, String middleName, String lastName) throws UserManagementException;

	void deleteUser(IUser<UserId> user) throws UserManagementException;

}
