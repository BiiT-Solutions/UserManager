package com.biit.usermanager.security;

import com.biit.usermanager.entity.IRole;

import java.util.Set;

/**
 * Relationship between a role and the activities that are assigned to this
 * role.
 */
public interface IRoleActivities<KEY> {

    Set<IActivity> getRoleActivities(IRole<KEY> role);

    Set<IActivity> getRoleActivities(String roleName);


}
