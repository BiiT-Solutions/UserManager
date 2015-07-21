package com.biit.usermanager.entity;

public interface IUser<Id> extends IElement<Id> {

	String getEmailAddress();

	String getPassword();

	String getUniqueName();

	void setPassword(String password);

}
