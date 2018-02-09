package com.noar.core.property.impl;

import org.springframework.context.MessageSource;

import com.noar.core.property.PropertyGroup;

@SuppressWarnings("all")
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