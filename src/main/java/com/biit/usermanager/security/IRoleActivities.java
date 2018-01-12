package com.biit.usermanager.security;

import java.util.Set;

import com.biit.usermanager.entity.IRole;

/**
 * Relationship between a role and the activities that are assigned to this
 * role.
 *
 */
public interface IRoleActivities {

	Set<IActivity> getRoleActivities(IRole<Long> role);
}
