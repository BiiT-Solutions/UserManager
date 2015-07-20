package com.biit.usermanager.entity.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;

public class RolePool<UserId, OrganizationId, RoleId> extends BasePool<RoleId, IRole<RoleId>> {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Map<UserId, Long> userTime; // user id -> time.
	private Map<UserId, List<IRole<RoleId>>> rolesByUser; // Roles by user.

	private Map<OrganizationId, Long> organizationTime; // Group Id -> time.
	private Map<OrganizationId, List<IRole<RoleId>>> rolesByOrganization; // Roles by group.

	private Map<UserId, Long> userRoleOfGroupTime; // user id -> time.
	private Map<UserId, Map<OrganizationId, List<IRole<RoleId>>>> userRolesOfGroup; // IUser<UserId>->Group->Roles.

	private RolePool() {
		reset();
	}

	public void addOrganizationRoles(IGroup<OrganizationId> organization, List<IRole<RoleId>> roles) {
		addOrganizationRoles(organization.getId(), roles);
	}

	private void addOrganizationRoles(OrganizationId organizationId, List<IRole<RoleId>> roles) {
		if (organizationId != null && roles != null) {
			organizationTime.put(organizationId, System.currentTimeMillis());
			List<IRole<RoleId>> groupRoles = rolesByOrganization.get(organizationId);
			if (groupRoles == null) {
				groupRoles = new ArrayList<IRole<RoleId>>();
				rolesByOrganization.put(organizationId, groupRoles);
			}

			for (IRole<RoleId> role : roles) {
				if (!groupRoles.contains(role)) {
					groupRoles.add(role);
				}
			}
		}
	}

	public void addUserGroupRole(IGroup<OrganizationId> group, IRole<RoleId> role) {
		if (group != null && role != null) {
			List<IRole<RoleId>> roles = new ArrayList<IRole<RoleId>>();
			roles.add(role);
			addOrganizationRoles(group, roles);
		}
	}

	public void addUserRole(IUser<UserId> user, IRole<RoleId> role) {
		if (user != null && role != null) {
			List<IRole<RoleId>> roles = new ArrayList<IRole<RoleId>>();
			roles.add(role);
			addUserRoles(user, roles);
		}
	}

	public void addUserRoles(IUser<UserId> user, List<IRole<RoleId>> roles) {
		if (user != null && roles != null && roles.size() > 0) {
			userTime.put(user.getId(), System.currentTimeMillis());
			List<IRole<RoleId>> userRoles = rolesByUser.get(user.getId());
			if (userRoles == null) {
				userRoles = new ArrayList<IRole<RoleId>>();
			}

			for (IRole<RoleId> role : roles) {
				if (!userRoles.contains(role)) {
					userRoles.add(role);
				}
			}

			rolesByUser.put(user.getId(), userRoles);
		}
	}

	public void addUserRolesOfOrganization(IUser<UserId> user, IGroup<OrganizationId> organization,
			List<IRole<RoleId>> roles) {
		if (user != null && organization != null) {
			addUserRolesOfOrganization(user.getId(), organization.getId(), roles);
		}
	}

	public void addUserRolesOfOrganization(UserId userId, OrganizationId groupId, List<IRole<RoleId>> roles) {
		if (userId != null && groupId != null && roles != null) {
			userRoleOfGroupTime.put(userId, System.currentTimeMillis());

			Map<OrganizationId, List<IRole<RoleId>>> userAndGroupRoles = userRolesOfGroup.get(userId);
			if (userAndGroupRoles == null) {
				userAndGroupRoles = new HashMap<OrganizationId, List<IRole<RoleId>>>();
				userRolesOfGroup.put(userId, userAndGroupRoles);
			}

			List<IRole<RoleId>> groupRoles = userAndGroupRoles.get(groupId);
			if (groupRoles == null) {
				groupRoles = new ArrayList<IRole<RoleId>>();
			}

			for (IRole<RoleId> role : roles) {
				if (!groupRoles.contains(role)) {
					groupRoles.add(role);
				}
			}
			userRolesOfGroup.get(userId).put(groupId, groupRoles);
		}
	}

	/**
	 * Get all roles of an organization.
	 * 
	 * @param group
	 * @return
	 */
	public List<IRole<RoleId>> getGroupRoles(IGroup<OrganizationId> organization) {
		return getOrganizationRoles(organization.getId());
	}

	/**
	 * Get all roles of a organization.
	 * 
	 * @param organization
	 * @return
	 */
	public List<IRole<RoleId>> getOrganizationRoles(IGroup<OrganizationId> organization) {
		return getOrganizationRoles(organization.getId());
	}

	/**
	 * Get all roles of a group.
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<IRole<RoleId>> getOrganizationRoles(OrganizationId organizationId) {
		if (organizationId != null) {
			long now = System.currentTimeMillis();
			OrganizationId nextOrganizationId = null;
			if (organizationTime.size() > 0) {
				Iterator<OrganizationId> e = new HashMap<OrganizationId, Long>(organizationTime).keySet().iterator();
				while (e.hasNext()) {
					nextOrganizationId = e.next();
					if ((now - organizationTime.get(nextOrganizationId)) > EXPIRATION_TIME) {
						// object has expired
						removeOrganizationRoles(nextOrganizationId);
						nextOrganizationId = null;
					} else {
						if (organizationId.equals(nextOrganizationId)) {
							return rolesByOrganization.get(nextOrganizationId);
						}
					}
				}
			}
		}
		return null;
	}

	public List<IRole<RoleId>> getUserRoles(IUser<UserId> user) {
		if (user != null) {
			long now = System.currentTimeMillis();
			UserId userId = null;
			if (userTime.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(userTime).keySet().iterator();
				while (e.hasNext()) {
					userId = e.next();
					if ((now - userTime.get(userId)) > EXPIRATION_TIME) {
						// object has expired
						removeUserRoles(userId);
						userId = null;
					} else {
						if (user.getId().equals(userId)) {
							return rolesByUser.get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public List<IRole<RoleId>> getUserRolesOfGroup(IUser<UserId> user, IGroup<OrganizationId> organization) {
		if (user != null && organization != null) {
			return getUserRolesOfOrganization(user.getId(), organization.getId());
		}
		return null;
	}

	public List<IRole<RoleId>> getUserRolesOfOrganization(UserId userId, OrganizationId organizationId) {
		if (userId != null && organizationId != null) {
			long now = System.currentTimeMillis();
			UserId nextUserId = null;
			if (userRoleOfGroupTime.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(userRoleOfGroupTime).keySet().iterator();
				while (e.hasNext()) {
					nextUserId = e.next();
					if ((now - userRoleOfGroupTime.get(nextUserId)) > EXPIRATION_TIME) {
						// object has expired
						removeUserRolesOfGroup(nextUserId);
						nextUserId = null;
					} else {
						if (userId.equals(nextUserId)) {
							Map<OrganizationId, List<IRole<RoleId>>> userAndOrganizationRoles = userRolesOfGroup
									.get(nextUserId);
							if (userAndOrganizationRoles != null) {
								return userAndOrganizationRoles.get(organizationId);
							}
						}
					}
				}
			}
		}
		return null;
	}

	public void removeOrganizationRole(IRole<RoleId> role, IGroup<OrganizationId> organization) {
		removeOrganizationRole(role, organization.getId());
	}

	public void removeOrganizationRole(IRole<RoleId> role, OrganizationId organizationId) {
		if (organizationId != null && rolesByOrganization.get(organizationId) != null) {
			rolesByOrganization.get(organizationId).remove(role);
		}
	}

	public void removeOrganizationRole(OrganizationId organizationId) {
		if (organizationId != null) {
			organizationTime.remove(organizationId);
			rolesByOrganization.remove(organizationId);
		}
	}

	public void removeOrganizationRoles(IGroup<OrganizationId> organization) {
		if (organization != null) {
			removeOrganizationRoles(organization.getId());
		}
	}

	public void removeOrganizationRoles(OrganizationId organizationId) {
		if (organizationId != null) {
			removeOrganizationRoles(organizationId);
		}
	}

	public void removeRole(IRole<RoleId> role) {
		List<IRole<RoleId>> roles = new ArrayList<IRole<RoleId>>();
		roles.add(role);
		removeRoles(roles);
	}

	public void removeRoles(List<IRole<RoleId>> roles) {
		for (IRole<RoleId> role : roles) {
			for (List<IRole<RoleId>> userRoles : rolesByUser.values()) {
				userRoles.remove(role);
			}
			for (List<IRole<RoleId>> groupRoles : rolesByOrganization.values()) {
				groupRoles.remove(role);
			}
			for (Map<OrganizationId, List<IRole<RoleId>>> rolesByUserAndGroup : userRolesOfGroup.values()) {
				for (List<IRole<RoleId>> userGroupRoles : rolesByUserAndGroup.values()) {
					userGroupRoles.remove(role);
				}
			}
		}
	}

	public void removeUserRole(IUser<UserId> user, IRole<RoleId> role) {
		if (user != null && role != null) {
			List<IRole<RoleId>> userRoles = rolesByUser.get(user.getId());
			if (userRoles != null) {
				userRoles.remove(role);
			}
		}
	}

	public void removeUserRoles(IUser<UserId> user) {
		if (user != null) {
			removeUserRoles(user.getId());
		}
	}

	public void removeUserRoles(UserId userId) {
		if (userId != null) {
			userTime.remove(userId);
			rolesByUser.remove(userId);
		}
	}

	public void removeUserRolesOfGroup(UserId userId) {
		if (userId != null) {
			userRoleOfGroupTime.remove(userId);
			userRolesOfGroup.remove(userId);
		}
	}

	@Override
	public void reset() {
		super.reset();
		userTime = new HashMap<UserId, Long>();
		organizationTime = new HashMap<OrganizationId, Long>();
		rolesByUser = new HashMap<UserId, List<IRole<RoleId>>>();
		rolesByOrganization = new HashMap<OrganizationId, List<IRole<RoleId>>>();
		userRoleOfGroupTime = new HashMap<UserId, Long>();
		userRolesOfGroup = new HashMap<UserId, Map<OrganizationId, List<IRole<RoleId>>>>();
	}

	public void setUserRoles(IUser<UserId> user, List<IRole<RoleId>> roles) {
		if (user != null && roles != null) {
			userTime.put(user.getId(), System.currentTimeMillis());
			rolesByUser.put(user.getId(), roles);
		}
	}
}
