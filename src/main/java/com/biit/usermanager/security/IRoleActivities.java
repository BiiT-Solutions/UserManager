package com.biit.usermanager.security;

import com.biit.usermanager.entity.IRole;

import java.util.Set;

/**
 * Relationship between a role and the activities that are assigned to this
 * role.
 */
public interface IRoleActivities<RoleId> {

    Set<IActivity> getRoleActivities(IRole<RoleId> role);

    Set<IActivity> getRoleActivities(String roleName);


}
