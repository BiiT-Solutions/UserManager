package com.biit.usermanager.entity;

/*-
 * #%L
 * User Manager Common Utils
 * %%
 * Copyright (C) 2015 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.Locale;

public interface IUser<Id> extends IElement<Id>, Comparable<IUser<Id>> {

	String getEmailAddress();

	String getFirstName();

	String getLanguageId();

	String getLastName();

	Locale getLocale();

	String getPassword();

	void setFirstName(String name);

	void setLastName(String surname);

	void setLocale(Locale locale);

	void setPassword(String password);

}
