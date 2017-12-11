package com.biit.usermanager.security;

public interface IActivity {

	public String getTag();

	IActivity getByName(String name);
}
