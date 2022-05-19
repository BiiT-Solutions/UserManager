package com.biit.usermanager.entity.pool;

import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.entity.pool.config.PoolConfigurationReader;
import com.biit.usermanager.security.IActivity;
import com.biit.utils.annotations.FindBugsSuppressWarnings;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Defines if an activity is authorized by an user or not.
 */
public class ActivityAuthorizationPool {

	// user id -> time.
	private Map<IUser<Long>, Long> time;
	// Form, user id -> activity -> allowed.
	private Map<IUser<Long>, Map<IActivity, Boolean>> users;
	private Map<IUser<Long>, Map<IGroup<Long>, Map<IActivity, Boolean>>> groups;

	public ActivityAuthorizationPool() {
		reset();
	}

	public void addUser(IUser<Long> user, IActivity activity, Boolean authorized) {
		if (user != null && activity != null && authorized != null) {
			users.computeIfAbsent(user, k -> new Hashtable<>());

			time.put(user, System.currentTimeMillis());
			users.get(user).put(activity, authorized);
		}
	}

	public void addUser(IUser<Long> user, IGroup<Long> organization, IActivity activity, Boolean authorized) {
		if (user != null && organization != null && activity != null && authorized != null) {
			groups.computeIfAbsent(user, k -> new HashMap<>());

			groups.get(user).computeIfAbsent(organization, k -> new HashMap<>());

			groups.get(user).get(organization).put(activity, authorized);
			time.put(user, System.currentTimeMillis());
		}
	}

	/**
	 * Returns true or false if the activity is authorized and null if is not
	 * catched.
	 *
	 * @param user
	 * @param activity
	 * @return
	 */
	@FindBugsSuppressWarnings("NP_BOOLEAN_RETURN_NULL")
	public Boolean isAuthorizedActivity(IUser<Long> user, IActivity activity) {
		long now = System.currentTimeMillis();
		IUser<Long> userForm;

		if (time.size() > 0) {
			for (IUser<Long> longIUser : new HashMap<>(time).keySet()) {
				userForm = longIUser;
				try {
					if (time.get(userForm) != null && (now - time.get(userForm)) > getExpirationTime()) {
						// object has expired
						removeUser(userForm);
					} else if (user != null && user.equals(userForm)) {
						if (users.get(user) != null && activity != null) {
							return users.get(user).get(activity);
						}
					}
				} catch (Exception except) {
					// Something is wrong. Considered as not cached.
				}
			}
		}
		return null;
	}

	@FindBugsSuppressWarnings("NP_BOOLEAN_RETURN_NULL")
	public Boolean isAuthorizedActivity(IUser<Long> user, IGroup<Long> organization, IActivity activity) {
		long now = System.currentTimeMillis();
		IUser<Long> authorizedUser;

		if (time.size() > 0) {
			for (IUser<Long> longIUser : new HashMap<>(time).keySet()) {
				authorizedUser = longIUser;
				try {
					if (time.get(authorizedUser) != null && (now - time.get(authorizedUser)) > getExpirationTime()) {
						// object has expired
						removeUser(authorizedUser);
					} else if (user != null && user.equals(authorizedUser)) {
						if (groups.get(user) != null && groups.get(user).get(organization) != null
								&& activity != null) {
							return groups.get(user).get(organization).get(activity);
						}
					}
				} catch (Exception except) {
					// Something is wrong. Considered as not cached.
				}
			}
		}
		return null;
	}

	public void removeUser(IUser<Long> user) {
		if (user != null) {
			time.remove(user);
			users.remove(user);
			groups.remove(user);
		}
	}

	public long getExpirationTime() {
		return PoolConfigurationReader.getInstance().getActivityPoolExpirationTime();
	}

	public void reset() {
		time = new HashMap<>();
		users = new HashMap<>();
		groups = new HashMap<>();
	}
}
