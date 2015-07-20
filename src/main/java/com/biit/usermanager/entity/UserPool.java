package com.biit.usermanager.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UserPool<UserId, RoleId> {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Map<UserId, Long> time; // User id -> time.
	private Map<UserId, IUser<UserId>> users; // User id -> User.

	private Map<RoleId, List<IUser<UserId>>> usersOfRole;
	private Map<RoleId, Long> usersOfRoleTime; // User id -> time.

	private UserPool() {
		reset();
	}

	public void reset() {
		time = new HashMap<UserId, Long>();
		users = new HashMap<UserId, IUser<UserId>>();
		usersOfRole = new HashMap<RoleId, List<IUser<UserId>>>();
		usersOfRoleTime = new HashMap<RoleId, Long>();
	}

	public void addUser(IUser<UserId> user) {
		if (user != null) {
			time.put(user.getUserId(), System.currentTimeMillis());
			users.put(user.getUserId(), user);
		}
	}

	public IUser<UserId> getUserByEmailAddress(String emailAddress) {
		if (emailAddress != null) {
			long now = System.currentTimeMillis();
			UserId userId = null;
			if (time.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(time).keySet().iterator();
				while (e.hasNext()) {
					userId = e.next();
					if ((now - time.get(userId)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(userId);
						userId = null;
					} else {
						if (users.get(userId) != null && users.get(userId).getEmailAddress().equals(emailAddress)) {
							return users.get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public IUser<UserId> getUserById(UserId userId) {
		if (userId != null) {
			long now = System.currentTimeMillis();
			UserId storedUserId = null;
			if (time.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(time).keySet().iterator();
				while (e.hasNext()) {
					storedUserId = e.next();
					if ((now - time.get(storedUserId)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(storedUserId);
						storedUserId = null;
					} else {
						if (users.get(storedUserId) != null && users.get(storedUserId).getUserId() == userId) {
							return users.get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public IUser<UserId> getUserByScreenName(String screenName) {
		if (screenName != null) {
			long now = System.currentTimeMillis();
			UserId userId = null;
			if (time.size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(time).keySet().iterator();
				while (e.hasNext()) {
					userId = e.next();
					if ((now - time.get(userId)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(userId);
						userId = null;
					} else {
						if (users.get(userId) != null && users.get(userId).getScreenName().equals(screenName)) {
							return users.get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public void removeUser(UserId userId) {
		if (userId != null) {
			time.remove(userId);
			users.remove(userId);
		}
	}

	public void removeUser(IUser<UserId> user) {
		if (user != null) {
			removeUser(user.getUserId());
		}
	}

	public List<IUser<UserId>> getUsersOfRole(RoleId roleId) {
		if (roleId != null) {
			long now = System.currentTimeMillis();
			RoleId storedObject = null;
			if (usersOfRoleTime.size() > 0) {
				Iterator<RoleId> e = new HashMap<RoleId, Long>(usersOfRoleTime).keySet().iterator();
				while (e.hasNext()) {
					storedObject = e.next();
					if ((now - usersOfRoleTime.get(storedObject)) > EXPIRATION_TIME) {
						// object has expired
						removeUsersOfRole(storedObject);
						storedObject = null;
					} else {
						if (storedObject.equals(roleId)) {
							return usersOfRole.get(storedObject);
						}
					}
				}
			}
		}
		return null;
	}

	public void addUsersOfRole(RoleId roleId, List<IUser<UserId>> usersOfRoles) {
		if (roleId != null && usersOfRoles != null) {
			usersOfRole.put(roleId, usersOfRoles);
			usersOfRoleTime.put(roleId, System.currentTimeMillis());
		}
	}

	public void removeUsersOfRole(RoleId roleId) {
		if (roleId != null) {
			usersOfRole.remove(roleId);
			usersOfRoleTime.remove(roleId);
		}
	}
}
