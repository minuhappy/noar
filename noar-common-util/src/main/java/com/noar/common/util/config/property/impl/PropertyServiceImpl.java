package com.noar.common.util.config.property.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;

import com.noar.common.util.config.property.PropertyGroup;
import com.noar.common.util.config.property.PropertyService;

public class PropertyServiceImpl implements PropertyService {

	private String defaultPropertyGroupName;
	private Map<String, PropertyGroup> propertyList = new HashMap<String, PropertyGroup>();
	private Map<String, MessageSource> propertyResources;

	public void setDefaultPropertyGroupName(String defaultPropertyGroupName) {
		this.defaultPropertyGroupName = defaultPropertyGroupName;
	}

	public String getDefaultPropertyGroupName() {
		return defaultPropertyGroupName;
	}

	public void setPropertyResources(Map<String, MessageSource> propertyResources) {
		this.propertyResources = propertyResources;
	}

	@Override
	public PropertyGroup getPropertyGroup(String propertyGroupName) {
		if (!propertyList.containsKey(propertyGroupName)) {
			PropertyGroup property = new PropertyGroupImpl(propertyGroupName, propertyResources.get(propertyGroupName));
			propertyList.put(propertyGroupName, property);
			return property;
		}
		return propertyList.get(propertyGroupName);
	}

	@Override
	public String getProperty(String groupName, String propertyName) {
		return getPropertyGroup(groupName).getProperty(propertyName);
	}

	@Override
	public String getProperty(String propertyName) {
		PropertyGroup propertyGroup = getPropertyGroup(defaultPropertyGroupName);
		return propertyGroup.getProperty(propertyName);
	}
}
