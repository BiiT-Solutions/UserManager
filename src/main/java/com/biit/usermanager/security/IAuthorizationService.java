package com.biit.usermanager.security;

import java.util.Set;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface IAuthorizationService<UserId, GroupId, RoleId> {

	Set<IUser<UserId>> getAllUsers() throws UserManagementException;

	Set<IUser<UserId>> getAllUsers(IGroup<GroupId> group) throws UserManagementException;

	IGroup<GroupId> getOrganization(long organizationId) throws UserManagementException;

	IRole<RoleId> getRole(Long roleId) throws UserManagementException;

	IRole<RoleId> getRole(String roleName) throws UserManagementException;

	Set<IActivity> getRoleActivities(IRole<RoleId> role);

	Set<IRole<RoleId>> getUserGroupRoles(IGroup<GroupId> group) throws UserManagementException;

	Set<IGroup<GroupId>> getUserGroups(IUser<UserId> user) throws UserManagementException;

	Set<IGroup<GroupId>> getUserOrganizations(IUser<UserId> user) throws UserManagementException;

	Set<IGroup<GroupId>> getUserOrganizations(IUser<UserId> user, IGroup<GroupId> site) throws UserManagementException;

	Set<IRole<RoleId>> getUserRoles(IUser<UserId> user) throws UserManagementException;

	Set<IRole<RoleId>> getUserRoles(IUser<UserId> user, IGroup<GroupId> organization) throws UserManagementException;

	boolean isAuthorizedActivity(IUser<UserId> user, IActivity activity) throws UserManagementException;

	boolean isAuthorizedActivity(IUser<UserId> user, IGroup<GroupId> organization, IActivity activity)
			throws UserManagementException;

	void reset();

}
