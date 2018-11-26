package com.noar.core.exception;

@SuppressWarnings("serial")
public class UnAuthorizedException extends SystemException {

	public UnAuthorizedException() {
		super();
	}

	public UnAuthorizedException(String message) {
		super(message);
	}

	public UnAuthorizedException(Throwable cause) {
		super(cause);
	}

	public UnAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}
}