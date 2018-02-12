package com.noar.common.util.config.property.impl;

import org.springframework.context.MessageSource;

import com.noar.common.util.config.property.PropertyGroup;

@SuppressWarnings("unused")
public class PropertyGroupImpl implements PropertyGroup {
	private String propertyName;
	private MessageSource propertySource;

	public PropertyGroupImpl(String propertyName, MessageSource propertySource) {
		this.propertyName = propertyName;
		this.propertySource = propertySource;
	}

	public String getProperty(String key) {
		return propertySource.getMessage(key, null, null);
	}
}