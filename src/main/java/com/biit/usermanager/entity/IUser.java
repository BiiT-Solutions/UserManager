package com.biit.usermanager.entity;

import java.util.Locale;

public interface IUser<Id> extends IElement<Id> {

	String getEmailAddress();

	String getPassword();

	String getUniqueName();

	void setPassword(String password);

	Locale getLocale();

	void setLocale(Locale locale);

	String getLanguageId();

	String getFirstName();

	String getLastName();

	String setSurname(String surname);

	String setName(String name);

}
