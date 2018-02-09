package com.noar.core.exception;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class SystemException extends RuntimeException {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemException.class);

	private String userMessage;
	private String detailMessage;
	private String msgDisplayLoc;

	private static Field messageField;

	private Field getMessageField() {
		if (messageField == null) {
			try {
				messageField = Throwable.class.getDeclaredField("detailMessage");
				if (!messageField.isAccessible()) {
					messageField.setAccessible(true);
				}
			} catch (SecurityException e) {
				LOGGER.warn(e.getMessage(), e);
			} catch (NoSuchFieldException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		return messageField;
	}

	public void setMessage(String message) {
		try {
			getMessageField().set(this, message);
		} catch (IllegalArgumentException e) {
			LOGGER.warn(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			LOGGER.warn(e.getMessage(), e);
		}
	}

	/**
	 * User Message.
	 */
	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}

	public String getMsgDisplayLoc() {
		return msgDisplayLoc;
	}

	public void setMsgDisplayLoc(String msgDisplayLoc) {
		this.msgDisplayLoc = msgDisplayLoc;
	}

	/**
	 * Constructor
	 */
	public SystemException() {
		super();
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}
}