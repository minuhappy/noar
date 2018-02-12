package com.noar.common.util.config.property;

public interface PropertyService {
	
	/**
	 * 프로퍼티 그룹을 조회하여 PropertyGroup을 리턴한다.
	 * 
	 * @param 프러퍼티 그룹 ID
	 * @return PropertyGroup
	 * @throws 
	 */
	public PropertyGroup getPropertyGroup(String propertyGroupName);
	
	/**
	 * 프로퍼티 그룹 ID 와 프로퍼티 Key 값으로 해당 key값의 value 조회서비스를 사용자에게 제공한다.
	 * 
	 * @param 프러퍼티 그룹 ID, 프러퍼티 Key
	 * @return String
	 * @throws 
	 */
	public String getProperty(String groupName, String propertyName);
	
	
	/**
	 * Default Property Group 을 기준으로 property를 조회.
	 * @param propertyName
	 * @return
	 */
	public String getProperty(String propertyName);
}
