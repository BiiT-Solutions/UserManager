package com.biit.usermanager.security;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;

/**
 * Defines if an activity is authorized by an user or not.
 */
public class AuthorizationPool<UserId, OrganizationId> {

	private final static long EXPIRATION_TIME = 300000;// 300 seconds

	// user id -> time.
	private Map<IUser<UserId>, Long> time;
	// Form, user id -> activity -> allowed.
	private Map<IUser<UserId>, Map<IActivity, Boolean>> activitiesByUser;
	private Map<IUser<UserId>, Map<IGroup<OrganizationId>, Map<IActivity, Boolean>>> activitiesByOrganization;

	public AuthorizationPool() {
		reset();
	}

	public void reset() {
		time = new HashMap<IUser<UserId>, Long>();
		activitiesByUser = new HashMap<IUser<UserId>, Map<IActivity, Boolean>>();
		activitiesByOrganization = new HashMap<IUser<UserId>, Map<IGroup<OrganizationId>, Map<IActivity, Boolean>>>();
	}

	/**
	 * Returns true or false if the activity is authorized and null if is not catched.
	 * 
	 * @param form
	 * @param user
	 * @param activity
	 * @return
	 */
	public Boolean isAuthorizedActivity(IUser<UserId> user, IActivity activity) {
		long now = System.currentTimeMillis();
		IUser<UserId> userForm = null;

		if (time.size() > 0) {
			Iterator<IUser<UserId>> userEnum = new HashMap<IUser<UserId>, Long>(time).keySet().iterator();
			while (userEnum.hasNext()) {
				userForm = userEnum.next();
				try {
					if (time.get(userForm) != null && (now - time.get(userForm)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(userForm);
						userForm = null;
					} else if (user != null && user.equals(userForm)) {
						if (activitiesByUser.get(user) != null && activity != null) {
							return activitiesByUser.get(user).get(activity);
						}
					}
				} catch (Exception except) {
					// Something is wrong. Considered as not cached.
				}
			}
		}
		return null;
	}

	public Boolean isAuthorizedActivity(IUser<UserId> user, IGroup<OrganizationId> organization,
			IActivity activity) {
		long now = System.currentTimeMillis();
		IUser<UserId> authorizedUser = null;

		if (time.size() > 0) {
			Iterator<IUser<UserId>> userEnum = new HashMap<IUser<UserId>, Long>(time).keySet().iterator();
			while (userEnum.hasNext()) {
				authorizedUser = userEnum.next();
				try {
					if (time.get(authorizedUser) != null && (now - time.get(authorizedUser)) > EXPIRATION_TIME) {
						// object has expired
						removeUser(authorizedUser);
						authorizedUser = null;
					} else if (user != null && user.equals(authorizedUser)) {
						if (activitiesByOrganization.get(user) != null && activitiesByOrganization.get(user).get(organization) != null
								&& activity != null) {
							return activitiesByOrganization.get(user).get(organization).get(activity);
						}
					}
				} catch (Exception except) {
					// Something is wrong. Considered as not cached.
				}
			}
		}
		return null;
	}

	public void addUser(IUser<UserId> user, IActivity activity, Boolean authorized) {
		if (user != null && activity != null && authorized != null) {
			if (activitiesByUser.get(user) == null) {
				activitiesByUser.put(user, new Hashtable<IActivity, Boolean>());
			}

			time.put(user, System.currentTimeMillis());
			activitiesByUser.get(user).put(activity, authorized);
		}
	}

	public void addUser(IUser<UserId> user, IGroup<OrganizationId> organization, IActivity activity,
			Boolean authorized) {
		if (user != null && organization != null && activity != null && authorized != null) {
			if (activitiesByOrganization.get(user) == null) {
				activitiesByOrganization.put(user, new HashMap<IGroup<OrganizationId>, Map<IActivity, Boolean>>());
			}

			if (activitiesByOrganization.get(user).get(organization) == null) {
				activitiesByOrganization.get(user).put(organization, new HashMap<IActivity, Boolean>());
			}

			activitiesByOrganization.get(user).get(organization).put(activity, authorized);
			time.put(user, System.currentTimeMillis());
		}
	}

	public void removeUser(IUser<UserId> user) {
		if (user != null) {
			time.remove(user);
			activitiesByUser.remove(user);
			activitiesByOrganization.remove(user);
		}
	}
}
