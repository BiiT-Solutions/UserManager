package com.biit.usermanager.entity;

public interface IUser<Id> extends IElement<Id> {

	String getEmailAddress();

	String getUniqueName();

}
