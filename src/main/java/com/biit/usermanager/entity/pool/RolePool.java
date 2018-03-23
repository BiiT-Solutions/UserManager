package com.biit.usermanager.entity.pool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.entity.pool.config.PoolConfigurationReader;

public class RolePool<UserId, GroupId, RoleId> extends ElementsByTagPool<RoleId, IRole<RoleId>> {

	private Map<UserId, Long> userTime; // user id -> time.
	private Map<UserId, Set<IRole<RoleId>>> rolesByUser; // Roles by user.

	private Map<GroupId, Long> groupTime; // Group Id -> time.
	private Map<GroupId, Set<IRole<RoleId>>> rolesByGroup; // Roles by group.

	private Map<UserId, Long> userRoleOfGroupTime; // user id -> time.
	private Map<UserId, Map<GroupId, Set<IRole<RoleId>>>> userRolesOfGroup; // IUser<UserId>->Group->Roles.

	public RolePool() {
		reset();
	}

	public void addGroupRole(IGroup<GroupId> group, IRole<RoleId> role) {
		if (group != null && role != null) {
			Set<IRole<RoleId>> roles = new HashSet<IRole<RoleId>>();
			roles.add(role);
			addGroupRoles(group, roles);
		}
	}

	public void addGroupRoles(GroupId groupId, Set<IRole<RoleId>> roles) {
		if (groupId != null && roles != null) {
			groupTime.put(groupId, System.currentTimeMillis());
			Set<IRole<RoleId>> groupRoles = rolesByGroup.get(groupId);
			if (groupRoles == null) {
				groupRoles = new HashSet<IRole<RoleId>>();
				rolesByGroup.put(groupId, groupRoles);
			}

			for (IRole<RoleId> role : roles) {
				if (!groupRoles.contains(role)) {
					groupRoles.add(role);
				}
			}
		}
	}

	public void addGroupRoles(IGroup<GroupId> group, Set<IRole<RoleId>> roles) {
		addGroupRoles(group.getUniqueId(), roles);
	}

	public void addUserRole(IUser<UserId> user, IRole<RoleId> role) {
		if (user != null && role != null) {
			Set<IRole<RoleId>> roles = new HashSet<IRole<RoleId>>();
			roles.add(role);
			addUserRoles(user, roles);
		}
	}

	public void addUserRoles(IUser<UserId> user, Set<IRole<RoleId>> roles) {
		if (user != null && roles != null && roles.size() > 0) {
			userTime.put(user.getUniqueId(), System.currentTimeMillis());
			Set<IRole<RoleId>> userRoles = rolesByUser.get(user.getUniqueId());
			if (userRoles == null) {
				userRoles = new HashSet<IRole<RoleId>>();
			}

			for (IRole<RoleId> role : roles) {
				if (!userRoles.contains(role)) {
					userRoles.add(role);
				}
			}

			rolesByUser.put(user.getUniqueId(), userRoles);
		}
	}

	public void addUserRolesOfGroup(IUser<UserId> user, IGroup<GroupId> group, Set<IRole<RoleId>> roles) {
		if (user != null && group != null) {
			addUserRolesOfGroup(user.getUniqueId(), group.getUniqueId(), roles);
		}
	}

	public void addUserRolesOfGroup(UserId userId, GroupId groupId, Set<IRole<RoleId>> roles) {
		if (userId != null && groupId != null && roles != null) {
			userRoleOfGroupTime.put(userId, System.currentTimeMillis());

			Map<GroupId, Set<IRole<RoleId>>> userAndGroupRoles = userRolesOfGroup.get(userId);
			if (userAndGroupRoles == null) {
				userAndGroupRoles = new HashMap<GroupId, Set<IRole<RoleId>>>();
				userRolesOfGroup.put(userId, userAndGroupRoles);
			}

			Set<IRole<RoleId>> groupRoles = userAndGroupRoles.get(groupId);
			if (groupRoles == null) {
				groupRoles = new HashSet<IRole<RoleId>>();
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
	 * Get all roles of a group.
	 * 
	 * @param groupId
	 * @return
	 */
	public Set<IRole<RoleId>> getGroupRoles(GroupId groupId) {
		if (groupId != null) {
			long now = System.currentTimeMillis();
			GroupId nextGroupId = null;
			if (groupTime.size() > 0) {
				Iterator<GroupId> e = new HashMap<GroupId, Long>(groupTime).keySet().iterator();
				while (e.hasNext()) {
					nextGroupId = e.next();
					if (groupTime.get(nextGroupId) != null
							&& (now - groupTime.get(nextGroupId)) > getExpirationTime()) {
						// object has expired
						removeGroupRoles(nextGroupId);
						nextGroupId = null;
					} else {
						if (groupId.equals(nextGroupId)) {
							return rolesByGroup.get(nextGroupId);
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Get all roles of an group.
	 * 
	 * @param group
	 * @return
	 */
	public Set<IRole<RoleId>> getGroupRoles(IGroup<GroupId> group) {
		return getGroupRoles(group.getUniqueId());
	}

	public Set<IRole<RoleId>> getUserRoles(IUser<UserId> user) {
		if (user != null) {
			long now = System.currentTimeMillis();
			UserId userId = null;
			if (userTime.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(userTime).keySet().iterator();
				while (e.hasNext()) {
					userId = e.next();
					if (userTime.get(userId) != null && (now - userTime.get(userId)) > getExpirationTime()) {
						// object has expired
						removeUserRoles(userId);
						userId = null;
					} else {
						if (user.getUniqueId().equals(userId)) {
							return rolesByUser.get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public Set<IRole<RoleId>> getUserRolesOfGroup(IUser<UserId> user, IGroup<GroupId> group) {
		if (user != null && group != null) {
			return getUserRolesOfGroup(user.getUniqueId(), group.getUniqueId());
		}
		return null;
	}

	public Set<IRole<RoleId>> getUserRolesOfGroup(UserId userId, GroupId groupId) {
		if (userId != null && groupId != null) {
			long now = System.currentTimeMillis();
			UserId nextId = null;
			if (userRoleOfGroupTime.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(userRoleOfGroupTime).keySet().iterator();
				while (e.hasNext()) {
					nextId = e.next();
					if (userRoleOfGroupTime.get(nextId) != null
							&& (now - userRoleOfGroupTime.get(nextId)) > getExpirationTime()) {
						// object has expired
						removeUserRolesOfGroup(nextId);
						nextId = null;
					} else {
						if (userId.equals(nextId)) {
							Map<GroupId, Set<IRole<RoleId>>> userAndGroupRoles = userRolesOfGroup.get(nextId);
							if (userAndGroupRoles != null) {
								return userAndGroupRoles.get(groupId);
							}
						}
					}
				}
			}
		}
		return null;
	}

	public void removeGroupRoles(GroupId groupId) {
		if (groupId != null) {
			groupTime.remove(groupId);
			rolesByGroup.remove(groupId);
		}
	}

	public void removeGroupRole(IRole<RoleId> role, GroupId groupId) {
		if (groupId != null && rolesByGroup.get(groupId) != null) {
			rolesByGroup.get(groupId).remove(role);
		}
	}

	public void removeGroupRole(IRole<RoleId> role, IGroup<GroupId> group) {
		removeGroupRole(role, group.getUniqueId());
	}

	public void removeGroupRoles(IGroup<GroupId> group) {
		if (group != null) {
			removeGroupRoles(group.getUniqueId());
		}
	}

	public void removeRole(IRole<RoleId> role) {
		Set<IRole<RoleId>> roles = new HashSet<IRole<RoleId>>();
		roles.add(role);
		removeRoles(roles);
	}

	public void removeRoles(Set<IRole<RoleId>> roles) {
		for (IRole<RoleId> role : roles) {
			for (Set<IRole<RoleId>> userRoles : rolesByUser.values()) {
				userRoles.remove(role);
			}
			for (Set<IRole<RoleId>> groupRoles : rolesByGroup.values()) {
				groupRoles.remove(role);
			}
			for (Map<GroupId, Set<IRole<RoleId>>> rolesByUserAndGroup : userRolesOfGroup.values()) {
				for (Set<IRole<RoleId>> userGroupRoles : rolesByUserAndGroup.values()) {
					userGroupRoles.remove(role);
				}
			}
		}
	}

	public void removeUserRole(IUser<UserId> user, IRole<RoleId> role) {
		if (user != null && role != null) {
			Set<IRole<RoleId>> userRoles = rolesByUser.get(user.getUniqueId());
			if (userRoles != null) {
				userRoles.remove(role);
			}
		}
	}

	public void removeUserRoles(IUser<UserId> user) {
		if (user != null) {
			removeUserRoles(user.getUniqueId());
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
	public long getExpirationTime() {
		return PoolConfigurationReader.getInstance().getRolePoolExpirationTime();
	}

	@Override
	public void reset() {
		super.reset();
		userTime = new HashMap<UserId, Long>();
		groupTime = new HashMap<GroupId, Long>();
		rolesByUser = new HashMap<UserId, Set<IRole<RoleId>>>();
		rolesByGroup = new HashMap<GroupId, Set<IRole<RoleId>>>();
		userRoleOfGroupTime = new HashMap<UserId, Long>();
		userRolesOfGroup = new HashMap<UserId, Map<GroupId, Set<IRole<RoleId>>>>();
	}

	public void setUserRoles(IUser<UserId> user, Set<IRole<RoleId>> roles) {
		if (user != null && roles != null) {
			userTime.put(user.getUniqueId(), System.currentTimeMillis());
			rolesByUser.put(user.getUniqueId(), roles);
		}
	}
}
