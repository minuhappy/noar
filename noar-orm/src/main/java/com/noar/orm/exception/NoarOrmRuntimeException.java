package com.noar.orm.exception;

@SuppressWarnings("serial")
public class NoarOrmRuntimeException extends RuntimeException {

	public NoarOrmRuntimeException() {
		super();
	}

	public NoarOrmRuntimeException(String message) {
		super(message);
	}

	public NoarOrmRuntimeException(Throwable cause) {
		super(cause);
	}

	public NoarOrmRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}