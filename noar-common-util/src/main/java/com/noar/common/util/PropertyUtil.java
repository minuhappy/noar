package com.noar.common.util;

import org.springframework.context.NoSuchMessageException;

import com.noar.common.util.config.property.CommonService;

public class PropertyUtil {

	/**
	 * Property 가져오기 실행.
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getProperty(String propertyName) {
		return getProperty(propertyName, null);
	}

	public static String getProperty(String propertyName, String defaultValue) {
		String value = ValueUtil.checkValue(System.getProperty(propertyName), getPropertyFromFile(propertyName));
		return ValueUtil.checkValue(value, defaultValue);
	}

	public static String getPropertyFromFile(String propertyName) {
		try {
			return CommonService.getPropertyService().getProperty(propertyName);
		} catch (NoSuchMessageException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}