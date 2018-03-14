package com.noar.common.util.config.property;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;

public class PropertyService {

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

	public PropertyGroup getPropertyGroup(String propertyGroupName) {
		if (!propertyList.containsKey(propertyGroupName)) {
			PropertyGroup property = new PropertyGroup(propertyGroupName, propertyResources.get(propertyGroupName));
			propertyList.put(propertyGroupName, property);
			return property;
		}
		return propertyList.get(propertyGroupName);
	}

	public String getProperty(String groupName, String propertyName) {
		return getPropertyGroup(groupName).getProperty(propertyName);
	}

	public String getProperty(String propertyName) {
		PropertyGroup propertyGroup = getPropertyGroup(defaultPropertyGroupName);
		return propertyGroup.getProperty(propertyName);
	}
}
