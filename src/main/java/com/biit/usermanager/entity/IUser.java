package com.biit.usermanager.entity;

import java.util.Locale;

public interface IUser<Id> extends IElement<Id> {

	String getEmailAddress();

	String getPassword();

	String getUniqueName();

	void setPassword(String password);

	Locale getLocale();

	String getLanguageId();

}
