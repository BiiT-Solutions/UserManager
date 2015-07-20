package com.biit.usermanager.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GroupPool<UserId, GroupId> {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	// Groups by id;
	private Map<GroupId, Long> groupTime; // user id -> time.
	private Map<GroupId, IGroup<GroupId>> groupsById;

	private Map<UserId, Long> userTime; // user id -> time.

	private Map<GroupId, Long> groupUsersTime; // group id -> time.
	private Map<GroupId, List<IUser<UserId>>> groupUsers; // Users by group.

	// User --> List<Group>
	private Map<UserId, Long> userGroupsTime;
	private Map<UserId, List<IGroup<GroupId>>> userGroups;

	public GroupPool() {
		reset();
	}

	public void reset() {
		userTime = new HashMap<UserId, Long>();
		groupUsersTime = new HashMap<GroupId, Long>();
		groupUsers = new HashMap<GroupId, List<IUser<UserId>>>();
		groupsById = new HashMap<GroupId, IGroup<GroupId>>();
		groupTime = new HashMap<GroupId, Long>();
		userGroups = new HashMap<UserId, List<IGroup<GroupId>>>();
		userGroupsTime = new HashMap<UserId, Long>();
	}

	public void addGroupUsers(GroupId groupId, List<IUser<UserId>> users) {
		if (groupId != null && users != null) {
			groupUsersTime.put(groupId, System.currentTimeMillis());
			List<IUser<UserId>> tempUsers = new ArrayList<IUser<UserId>>(users);
			groupUsers.put(groupId, tempUsers);
		}
	}

	public void addGroup(IGroup<GroupId> group) {
		groupTime.put(group.getGroupId(), System.currentTimeMillis());
		groupsById.put(group.getGroupId(), group);
	}

	/**
	 * Gets all previously stored groups of a user in a site.
	 * 
	 * @param siteId
	 * @param userId
	 * @return
	 */
	public IGroup<GroupId> getGroupById(GroupId groupId) {
		if (groupId != null) {
			long now = System.currentTimeMillis();
			GroupId storedObjectId = null;
			if (groupTime.size() > 0) {
				Iterator<GroupId> groupsIds = new HashMap<GroupId, Long>(groupTime).keySet().iterator();
				while (groupsIds.hasNext()) {
					storedObjectId = groupsIds.next();
					if ((now - groupTime.get(storedObjectId)) > EXPIRATION_TIME) {
						// object has expired
						removeGroupsById(groupId);
						storedObjectId = null;
					} else {
						if (groupsById.get(storedObjectId) != null && storedObjectId == groupId) {
							return groupsById.get(storedObjectId);
						}
					}
				}
			}
		}
		return null;
	}

	public List<IUser<UserId>> getGroupUsers(GroupId groupId) {
		if (groupId != null) {
			long now = System.currentTimeMillis();
			GroupId nextGroupId = null;
			if (groupUsersTime.size() > 0) {
				Iterator<GroupId> e = new HashMap<GroupId, Long>(groupUsersTime).keySet().iterator();
				while (e.hasNext()) {
					nextGroupId = e.next();
					if ((now - groupUsersTime.get(nextGroupId)) > EXPIRATION_TIME) {
						// Object has expired.
						removeGroupUsers(nextGroupId);
						nextGroupId = null;
					} else {
						if (groupId == nextGroupId) {
							return groupUsers.get(nextGroupId);
						}
					}
				}
			}
		}
		return null;
	}

	public void removeGroups(IUser<UserId> user) {
		if (user != null) {
			removeGroupsOfUser(user.getUserId());
		}
	}

	private void removeGroupsOfUser(UserId userId) {
		if (userId != null) {
			userTime.remove(userId);
		}
	}

	public void removeGroupUsers(GroupId groupId) {
		if (groupId != null) {
			groupUsersTime.remove(groupId);
			groupUsers.remove(groupId);
		}
	}

	public void removeUserGroups(UserId userId) {
		if (userId != null) {
			userGroupsTime.remove(userId);
			userGroups.remove(userId);
		}
	}

	public void removeGroupsById(GroupId groupId) {
		if (groupId != null) {
			groupTime.remove(groupId);
			groupsById.remove(groupId);
		}
	}

	public void removeUserFromGroups(UserId userId, GroupId groupId) {
		if (userId != null && groupId != null) {
			List<IUser<UserId>> tempUsers = new ArrayList<IUser<UserId>>(groupUsers.get(groupId));
			for (IUser<UserId> user : tempUsers) {
				if (user.getUserId() == userId) {
					groupUsers.get(groupId).remove(user);
				}
			}
		}
	}

	public void removeUserFromGroups(IUser<UserId> user, IGroup<GroupId> group) {
		if (user != null && group != null) {
			removeUserFromGroups(user.getUserId(), group.getGroupId());
		}
	}

	public List<IGroup<GroupId>> getGroups(UserId userId) {
		if (userId != null) {
			long now = System.currentTimeMillis();
			UserId nextUserId = null;
			if (userGroupsTime.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(userGroupsTime).keySet().iterator();
				while (e.hasNext()) {
					nextUserId = e.next();
					if ((now - userGroupsTime.get(nextUserId)) > EXPIRATION_TIME) {
						// Object has expired.
						removeUserGroups(nextUserId);
						nextUserId = null;
					} else {
						if (userId == nextUserId) {
							return userGroups.get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public void addGroups(UserId userId, List<IGroup<GroupId>> groups) {
		if (userId != null && groups != null) {
			userGroupsTime.put(userId, System.currentTimeMillis());
			List<IGroup<GroupId>> tempGroups = new ArrayList<IGroup<GroupId>>(groups);
			userGroups.put(userId, tempGroups);
		}
	}
}
