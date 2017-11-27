package com.minu.core.property.impl;

import java.util.HashMap;

import org.springframework.context.MessageSource;

import com.minu.core.property.PropertyGroup;
import com.minu.core.property.PropertyService;


public class PropertyServiceImpl implements PropertyService{
	
	private String defaultPropertyGroupName;
	private java.util.Map<String, PropertyGroup> propertyList = new HashMap<String, PropertyGroup>();
	private java.util.Map<String, MessageSource> propertyResources;
	
	public void setDefaultPropertyGroupName(String defaultPropertyGroupName) {
		this.defaultPropertyGroupName = defaultPropertyGroupName;
	}
	
	public String getDefaultPropertyGroupName() {
		return defaultPropertyGroupName;
	}
	
	public void setPropertyResources(
			java.util.Map<String, MessageSource> propertyResources) {
		this.propertyResources = propertyResources;
	}	
	
	@Override
	public PropertyGroup getPropertyGroup(String propertyGroupName) {
		if(!propertyList.containsKey(propertyGroupName)) {
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
