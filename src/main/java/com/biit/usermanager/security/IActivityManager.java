package com.biit.usermanager.security;

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
