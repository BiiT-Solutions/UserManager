package com.biit.usermanager.entity.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biit.usermanager.entity.IUser;

public class UserPool<UserId, RoleId> extends BasePool<UserId, IUser<UserId>> {

	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Map<RoleId, List<IUser<UserId>>> usersOfRole;
	private Map<RoleId, Long> usersOfRoleTime; // User id -> getElementsTime().

	public UserPool() {
		reset();
	}

	public void addUser(IUser<UserId> user) {
		addElement(user);
	}

	public void addUsersOfRole(RoleId roleId, List<IUser<UserId>> usersOfRoles) {
		if (roleId != null && usersOfRoles != null) {
			usersOfRole.put(roleId, usersOfRoles);
			usersOfRoleTime.put(roleId, System.currentTimeMillis());
		}
	}

	public IUser<UserId> getUserByEmailAddress(String emailAddress) {
		if (emailAddress != null) {
			long now = System.currentTimeMillis();
			UserId userId = null;
			if (getElementsTime().size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(getElementsTime()).keySet().iterator();
				while (e.hasNext()) {
					userId = e.next();
					if (getElementsTime().get(userId) != null
							&& (now - getElementsTime().get(userId)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(userId);
						userId = null;
					} else {
						if (getElementsById().get(userId) != null
								&& getElementsById().get(userId).getEmailAddress().equals(emailAddress)) {
							return getElementsById().get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public IUser<UserId> getUserById(UserId userId) {
		return getElement(userId);
	}

	public IUser<UserId> getUserByScreenName(String screenName) {
		if (screenName != null) {
			long now = System.currentTimeMillis();
			UserId userId = null;
			if (getElementsTime().size() > 0) {
				Iterator<UserId> e = new HashMap<UserId, Long>(getElementsTime()).keySet().iterator();
				while (e.hasNext()) {
					userId = e.next();
					if (getElementsTime().get(userId) != null
							&& (now - getElementsTime().get(userId)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(userId);
						userId = null;
					} else {
						if (getElementsById().get(userId) != null
								&& getElementsById().get(userId).getUniqueName().equals(screenName)) {
							return getElementsById().get(userId);
						}
					}
				}
			}
		}
		return null;
	}

	public List<IUser<UserId>> getUsersOfRole(RoleId roleId) {
		if (roleId != null) {
			long now = System.currentTimeMillis();
			RoleId storedObject = null;
			if (usersOfRoleTime.size() > 0) {
				Iterator<RoleId> e = new HashMap<RoleId, Long>(usersOfRoleTime).keySet().iterator();
				while (e.hasNext()) {
					storedObject = e.next();
					if (usersOfRoleTime.get(storedObject) != null
							&& (now - usersOfRoleTime.get(storedObject)) > EXPIRATION_TIME) {
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

	public void removeUser(IUser<UserId> user) {
		if (user != null) {
			removeUser(user.getId());
		}
	}

	public void removeUser(UserId userId) {
		removeElement(userId);
	}

	public void removeUsersOfRole(RoleId roleId) {
		if (roleId != null) {
			usersOfRole.remove(roleId);
			usersOfRoleTime.remove(roleId);
		}
	}

	@Override
	public void reset() {
		super.reset();
		usersOfRole = new HashMap<RoleId, List<IUser<UserId>>>();
		usersOfRoleTime = new HashMap<RoleId, Long>();
	}
}
