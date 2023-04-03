package com.biit.usermanager.security;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.*;

import java.util.Set;

public interface IAuthorizationService<UserId, GroupId, RoleId> {

    /**
     * Get all users.
     *
     * @return
     * @throws UserManagementException
     */
    Set<IUser<UserId>> getAllUsers() throws UserManagementException, InvalidCredentialsException;

    /**
     * Get all users from an organization.
     */
    Set<IUser<UserId>> getAllUsers(IGroup<GroupId> group) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException;

    /**
     * Gets the organization from this id.
     *
     * @param organizationId
     * @return
     * @throws UserManagementException
     */
    IGroup<GroupId> getOrganization(GroupId organizationId) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException;

    /**
     * Gets the organization from its name.
     *
     * @param organizationName
     * @return
     * @throws UserManagementException
     */
    IGroup<GroupId> getOrganization(String organizationName) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException;

    /**
     * Gets all the organizations of a company
     *
     * @return
     * @throws UserManagementException
     */
    Set<IGroup<GroupId>> getAllAvailableOrganizations() throws UserManagementException, InvalidCredentialsException;

    /**
     * Gets a role from this id
     *
     * @param roleId
     * @return
     * @throws UserManagementException
     */
    IRole<RoleId> getRole(RoleId roleId) throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException;

    /**
     * Get a role from its name.
     *
     * @param roleName
     * @return
     * @throws UserManagementException
     * @throws RoleDoesNotExistsException
     */
    IRole<RoleId> getRole(String roleName) throws UserManagementException, RoleDoesNotExistsException, InvalidCredentialsException;

    /**
     * Get the roles for this group.
     *
     * @param group
     * @return
     * @throws UserManagementException
     */
    Set<IRole<RoleId>> getUserGroupRoles(IGroup<GroupId> group) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException;

    /**
     * Get all user groups
     *
     * @param user
     * @return
     * @throws UserManagementException
     */
    Set<IGroup<GroupId>> getUserGroups(IUser<UserId> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    /**
     * get all user organizations.
     *
     * @param user
     * @return
     * @throws UserManagementException
     */
    Set<IGroup<GroupId>> getUserOrganizations(IUser<UserId> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    /**
     * Get all user organizations for this site.
     *
     * @param user
     * @param site
     * @return
     * @throws UserManagementException
     */
    Set<IGroup<GroupId>> getUserOrganizations(IUser<UserId> user, IGroup<GroupId> site) throws UserManagementException, UserDoesNotExistException,
            OrganizationDoesNotExistException, InvalidCredentialsException;

    /**
     * Get the user roles of the application.
     *
     * @param user
     * @return
     * @throws UserManagementException
     */
    Set<IRole<RoleId>> getUserRoles(IUser<UserId> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    /**
     * Get user roles of user in a group
     *
     * @param user
     * @param organization
     * @return
     * @throws UserManagementException
     */
    Set<IRole<RoleId>> getUserRoles(IUser<UserId> user, IGroup<GroupId> organization) throws UserManagementException, UserDoesNotExistException,
            OrganizationDoesNotExistException, InvalidCredentialsException;

    /**
     * Gets all available roles.
     *
     * @return
     * @throws UserManagementException
     */
    Set<IRole<RoleId>> getAllRoles(IGroup<Long> organization) throws UserManagementException, OrganizationDoesNotExistException, InvalidCredentialsException;

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
    Set<IUser<UserId>> getUsers(IRole<RoleId> role, IGroup<GroupId> organization) throws UserManagementException, RoleDoesNotExistsException,
            OrganizationDoesNotExistException, InvalidCredentialsException;

    /**
     * Return all user's organization of the application that is not a
     * suborganization. An organization is valid if it has a role that exists in
     * the application and has no parent organization.
     *
     * @param user the selected user.
     * @return a set of organizations.
     * @throws UserManagementException
     */
    Set<IGroup<Long>> getUserParentOrganizations(IUser<Long> user) throws UserManagementException, UserDoesNotExistException, InvalidCredentialsException;

    /**
     * Return all user's organization of the application that is a
     * suborganization. An organization is valid if it has a role that exists in
     * the application and has a parent organization.
     *
     * @param user               the selected user
     * @param parentOrganization the parent organization.
     * @return a set of organizations
     * @throws UserManagementException
     */
    Set<IGroup<Long>> getUserChildrenOrganizations(IUser<UserId> user, IGroup<GroupId> parentOrganization) throws UserManagementException, UserDoesNotExistException, OrganizationDoesNotExistException,
            InvalidCredentialsException;

    void addUserRole(IUser<UserId> user, IRole<RoleId> role) throws UserManagementException, UserDoesNotExistException, RoleDoesNotExistsException, InvalidCredentialsException;

    void addUserOrganizationRole(IUser<UserId> user, IGroup<GroupId> organization, IRole<RoleId> role) throws UserManagementException, UserDoesNotExistException,
            RoleDoesNotExistsException, OrganizationDoesNotExistException, InvalidCredentialsException;

    void createBeans();

    void cleanUserChildrenOrganizations(IUser<Long> user, IGroup<Long> parentOrganization) throws UserManagementException, UserDoesNotExistException, OrganizationDoesNotExistException,
            InvalidCredentialsException;

}
