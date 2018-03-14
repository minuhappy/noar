package com.noar.common.util.config.property;

import org.springframework.context.MessageSource;

@SuppressWarnings("unused")
public class PropertyGroup {
	private String propertyName;
	private MessageSource propertySource;

	public PropertyGroup(String propertyName, MessageSource propertySource) {
		this.propertyName = propertyName;
		this.propertySource = propertySource;
	}

	public String getProperty(String key) {
		return propertySource.getMessage(key, null, null);
	}
}