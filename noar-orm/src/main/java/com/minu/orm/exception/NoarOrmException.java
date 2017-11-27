package com.minu.orm.exception;

@SuppressWarnings("serial")
public class NoarOrmException extends Exception {

	public NoarOrmException() {
		super();
	}

	public NoarOrmException(String message) {
		super(message);
	}

	public NoarOrmException(Throwable cause) {
		super(cause);
	}

	public NoarOrmException(String message, Throwable cause) {
		super(message, cause);
	}
}