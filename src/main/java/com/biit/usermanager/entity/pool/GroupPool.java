package com.biit.usermanager.entity.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;

public class GroupPool<UserId, GroupId> extends BasePool<GroupId, IGroup<GroupId>> {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	// Group --> List<User>
	private Map<GroupId, Long> groupUsersTime; // group id -> time.
	private Map<GroupId, Set<IUser<UserId>>> groupUsers; // Users by group.

	// User --> List<Group>
	private Map<UserId, Long> userGroupsTime;
	private Map<UserId, Set<IGroup<GroupId>>> userGroups;

	public GroupPool() {
		reset();
	}

	public void addGroup(IGroup<GroupId> group) {
		addElement(group);
	}

	public void addGroupByTag(IGroup<GroupId> group, String tag) {
		if (tag != null && group != null) {
			super.addElementByTag(group, tag);
		}
	}

	public void addGroupByTag(Set<IGroup<GroupId>> groups, String tag) {
		if (tag != null && groups != null) {
			super.addElementByTag(groups, tag);
		}
	}

	public void addGroupUsers(GroupId groupId, Set<IUser<UserId>> users) {
		if (groupId != null && users != null) {
			Set<IUser<UserId>> usersOfGroup = getGroupUsers(groupId);
			if (usersOfGroup == null) {
				usersOfGroup = new HashSet<IUser<UserId>>();
			}

			usersOfGroup.addAll(users);
			groupUsersTime.put(groupId, System.currentTimeMillis());
			groupUsers.put(groupId, usersOfGroup);
		}
	}

	public void addUserToGroup(IUser<UserId> user, IGroup<GroupId> group) {
		if (user != null && group != null) {
			Set<IGroup<GroupId>> groups = getGroups(user.getId());
			if (groups == null) {
				groups = new HashSet<IGroup<GroupId>>();
			}
			groups.add(group);
			userGroupsTime.put(user.getId(), System.currentTimeMillis());
			userGroups.put(user.getId(), groups);

//			Set<IUser<UserId>> users = new HashSet<IUser<UserId>>();
//			users.add(user);
//			addGroupUsers(group.getId(), users);
		}
	}

	public void addUserToGroups(IUser<UserId> user, Set<IGroup<GroupId>> groups) {
		if (user != null && groups != null) {
			for (IGroup<GroupId> group : groups) {
				addUserToGroup(user, group);
			}
		}
	}

	/**
	 * Gets all previously stored groups of a user in a site.
	 * 
	 * @param siteId
	 * @param userId
	 * @return
	 */
	public IGroup<GroupId> getGroupById(GroupId groupId) {
		return getElement(groupId);
	}

	public Set<IGroup<GroupId>> getGroups(UserId groupId) {
		if (groupId != null) {
			long now = System.currentTimeMillis();
			UserId nextGroupId = null;
			if (userGroupsTime.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(userGroupsTime).keySet().iterator();
				while (e.hasNext()) {
					nextGroupId = e.next();
					if (userGroupsTime.get(nextGroupId) != null
							&& (now - userGroupsTime.get(nextGroupId)) > EXPIRATION_TIME) {
						// Object has expired.
						removeUserGroups(nextGroupId);
						nextGroupId = null;
					} else {
						if (groupId.equals(nextGroupId)) {
							return userGroups.get(groupId);
						}
					}
				}
			}
		}
		return null;
	}

	public Set<IUser<UserId>> getGroupUsers(GroupId groupId) {
		if (groupId != null) {
			long now = System.currentTimeMillis();
			GroupId nextGroupId = null;
			if (groupUsersTime.size() > 0) {
				Iterator<GroupId> e = new HashMap<GroupId, Long>(groupUsersTime).keySet().iterator();
				while (e.hasNext()) {
					nextGroupId = e.next();
					if (groupUsersTime.get(nextGroupId) != null
							&& (now - groupUsersTime.get(nextGroupId)) > EXPIRATION_TIME) {
						// Object has expired.
						removeGroupUsers(nextGroupId);
						nextGroupId = null;
					} else {
						if (groupId.equals(nextGroupId)) {
							return groupUsers.get(nextGroupId);
						}
					}
				}
			}
		}
		return null;
	}

	public void removeGroupByTag(String tag, IGroup<Long> group) {
		super.removeElementsByTag(tag, group);
	}

	public void removeGroupsById(GroupId groupId) {
		removeElement(groupId);
	}

	public void removeGroupsByTag(String tag) {
		super.removeElementsByTag(tag);
	}

	public void removeGroupUsers(GroupId groupId) {
		if (groupId != null) {
			groupUsersTime.remove(groupId);
			groupUsers.remove(groupId);
		}
	}

	public void removeUser(IUser<UserId> user) {
		if (user != null) {
			removeUserGroups(user.getId());
			for (GroupId groupId : groupUsers.keySet()) {
				removeUserFromGroups(user.getId(), groupId);
			}
		}
	}

	public void removeUserFromGroups(IUser<UserId> user, IGroup<GroupId> group) {
		if (user != null && group != null) {
			removeUserFromGroups(user.getId(), group.getId());
		}
	}

	public void removeUserFromGroups(UserId userId, GroupId groupId) {
		if (userId != null && groupId != null) {
			if (groupUsers.get(groupId) != null) {
				List<IUser<UserId>> tempUsers = new ArrayList<IUser<UserId>>(groupUsers.get(groupId));
				for (IUser<UserId> user : tempUsers) {
					if (user.getId().equals(userId)) {
						groupUsers.get(groupId).remove(user);
					}
				}
			}
			if (userGroups.get(userId) != null) {
				for (IGroup<GroupId> group : new HashSet<IGroup<GroupId>>(userGroups.get(userId))) {
					if (group.getId().equals(groupId)) {
						userGroups.get(userId).remove(group);
					}
				}
			}
		}
	}

	public void removeUserGroups(UserId userId) {
		if (userId != null) {
			userGroupsTime.remove(userId);
			userGroups.remove(userId);
		}
	}

	@Override
	public void reset() {
		super.reset();
		groupUsersTime = new HashMap<GroupId, Long>();
		groupUsers = new HashMap<GroupId, Set<IUser<UserId>>>();
		userGroups = new HashMap<UserId, Set<IGroup<GroupId>>>();
		userGroupsTime = new HashMap<UserId, Long>();
	}

}
