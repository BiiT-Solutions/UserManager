package com.biit.usermanager.security;

import java.util.Set;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.UserManagementException;

public interface IAuthorizationService<UserId, GroupId, RoleId> {

	/**
	 * Get all users.
	 * 
	 * @return
	 * @throws UserManagementException
	 */
	Set<IUser<UserId>> getAllUsers() throws UserManagementException;

	/**
	 * Get all users from an organization.
	 */
	Set<IUser<UserId>> getAllUsers(IGroup<GroupId> group) throws UserManagementException;

	/**
	 * Gets the organization from this id.
	 * 
	 * @param organizationId
	 * @return
	 * @throws UserManagementException
	 */
	IGroup<GroupId> getOrganization(GroupId organizationId) throws UserManagementException;

	/**
	 * Gets a role from this id
	 * 
	 * @param roleId
	 * @return
	 * @throws UserManagementException
	 */
	IRole<RoleId> getRole(RoleId roleId) throws UserManagementException;

	/**
	 * Get a role from its name.
	 * 
	 * @param roleName
	 * @return
	 * @throws UserManagementException
	 */
	IRole<RoleId> getRole(String roleName) throws UserManagementException;

	/**
	 * Get the activities associated to this role.S *
	 * 
	 * @param role
	 * @return
	 */
	Set<IActivity> getRoleActivities(IRole<RoleId> role);

	/**
	 * Get the roles for this group.
	 * 
	 * @param group
	 * @return
	 * @throws UserManagementException
	 */
	Set<IRole<RoleId>> getUserGroupRoles(IGroup<GroupId> group) throws UserManagementException;

	/**
	 * Get all user groups
	 * 
	 * @param user
	 * @return
	 * @throws UserManagementException
	 */
	Set<IGroup<GroupId>> getUserGroups(IUser<UserId> user) throws UserManagementException;

	/**
	 * get all user organizations.
	 * 
	 * @param user
	 * @return
	 * @throws UserManagementException
	 */
	Set<IGroup<GroupId>> getUserOrganizations(IUser<UserId> user) throws UserManagementException;

	/**
	 * Get all user organizations for this site.S
	 * 
	 * @param user
	 * @param site
	 * @return
	 * @throws UserManagementException
	 */
	Set<IGroup<GroupId>> getUserOrganizations(IUser<UserId> user, IGroup<GroupId> site) throws UserManagementException;

	/**
	 * Get the user roles of the application.
	 * 
	 * @param user
	 * @return
	 * @throws UserManagementException
	 */
	Set<IRole<RoleId>> getUserRoles(IUser<UserId> user) throws UserManagementException;

	/**
	 * Get user roles of user in a group
	 * 
	 * @param user
	 * @param organization
	 * @return
	 * @throws UserManagementException
	 */
	Set<IRole<RoleId>> getUserRoles(IUser<UserId> user, IGroup<GroupId> organization) throws UserManagementException;

	/**
	 * A user is authorized to perform this activity in the application.
	 * 
	 * @param user
	 * @param activity
	 * @return
	 * @throws UserManagementException
	 */
	boolean isAuthorizedActivity(IUser<UserId> user, IActivity activity) throws UserManagementException;

	/**
	 * A user is authorized to perform an activity in this group.
	 * 
	 * @param user
	 * @param organization
	 * @param activity
	 * @return
	 * @throws UserManagementException
	 */
	boolean isAuthorizedActivity(IUser<UserId> user, IGroup<GroupId> organization, IActivity activity)
			throws UserManagementException;

	/**
	 * Clear all cached instances.
	 */
	void reset();

	/**
	 * Get all users that have a role in an organization.
	 * 
	 * @param role
	 * @param organization
	 * @return
	 * @throws UserManagementException
	 */
	Set<IUser<UserId>> getUsers(IRole<RoleId> role, IGroup<GroupId> organization) throws UserManagementException;

}
