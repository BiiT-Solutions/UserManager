package com.biit.usermanager.entity;

public interface IUser<Id> {

	Id getUserId();

	String getEmailAddress();

	String getScreenName();

}
