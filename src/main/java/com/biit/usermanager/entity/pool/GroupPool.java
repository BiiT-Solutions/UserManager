package com.biit.usermanager.entity.pool;

/*-
 * #%L
 * User Manager Common Utils
 * %%
 * Copyright (C) 2015 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.entity.pool.config.PoolConfigurationReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroupPool<UserId, GroupId> extends ElementsByTagPool<GroupId, IGroup<GroupId>> {
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
            Set<IGroup<GroupId>> groups = getGroups(user.getUniqueId());
            if (groups == null) {
                groups = new HashSet<IGroup<GroupId>>();
            }
            groups.add(group);
            userGroupsTime.put(user.getUniqueId(), System.currentTimeMillis());
            userGroups.put(user.getUniqueId(), groups);

            // Set<IUser<UserId>> users = new HashSet<IUser<UserId>>();
            // users.add(user);
            // addGroupUsers(group.getId(), users);
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
     * @param groupId
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
                            && (now - userGroupsTime.get(nextGroupId)) > getExpirationTime()) {
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
                            && (now - groupUsersTime.get(nextGroupId)) > getExpirationTime()) {
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
            removeUserGroups(user.getUniqueId());
            for (GroupId groupId : groupUsers.keySet()) {
                removeUserFromGroups(user.getUniqueId(), groupId);
            }
        }
    }

    public void removeUserFromGroups(IUser<UserId> user, IGroup<GroupId> group) {
        if (user != null && group != null) {
            removeUserFromGroups(user.getUniqueId(), group.getUniqueId());
        }
    }

    public void removeUserFromGroups(UserId userId, GroupId groupId) {
        if (userId != null && groupId != null) {
            if (groupUsers.get(groupId) != null) {
                List<IUser<UserId>> tempUsers = new ArrayList<IUser<UserId>>(groupUsers.get(groupId));
                for (IUser<UserId> user : tempUsers) {
                    if (user.getUniqueId().equals(userId)) {
                        groupUsers.get(groupId).remove(user);
                    }
                }
            }
            if (userGroups.get(userId) != null) {
                for (IGroup<GroupId> group : new HashSet<IGroup<GroupId>>(userGroups.get(userId))) {
                    if (group.getUniqueId().equals(groupId)) {
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
    public long getExpirationTime() {
        return PoolConfigurationReader.getInstance().getGroupPoolExpirationTime();
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
