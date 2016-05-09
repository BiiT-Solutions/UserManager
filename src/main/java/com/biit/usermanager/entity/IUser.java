package com.biit.usermanager.entity;

import java.util.Locale;

public interface IUser<Id> extends IElement<Id> {

	String getEmailAddress();

	String getFirstName();

	String getLanguageId();

	String getLastName();

	Locale getLocale();

	String getPassword();

	String getUniqueName();

	void setFirstname(String name);

	void setLastname(String surname);

	void setLocale(Locale locale);

	void setPassword(String password);

}
