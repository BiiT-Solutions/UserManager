package com.biit.usermanager.security.exceptions;

public class RoleDoesNotExistsException extends Exception {
	private static final long serialVersionUID = 8303590976450383259L;

	public RoleDoesNotExistsException(String message) {
		super(message);
	}

}
