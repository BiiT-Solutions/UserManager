package com.biit.usermanager.security.exceptions;

public class AuthenticationRequired extends Exception {
	private static final long serialVersionUID = -4477622972571376111L;

	public AuthenticationRequired(String info) {
		super(info);
	}
}
