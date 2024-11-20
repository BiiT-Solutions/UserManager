package com.biit.usermanager.entity;

import java.util.Locale;

public interface IUser<Id> extends IElement<Id>, Comparable<IUser<Id>> {

    String getEmailAddress();

    String getFirstName();

    String getLastName();

    Locale getLocale();

    String getPassword();

    void setFirstName(String name);

    void setLastName(String surname);

    void setLocale(Locale locale);

    void setPassword(String password);

}
