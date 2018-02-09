package com.noar.core.util;

import org.springframework.context.NoSuchMessageException;

import com.noar.core.property.CommonService;

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
		String value = System.getProperty(propertyName);

		if (ValueUtil.isEmpty(value)) {
			value = getPropertyFromDB(propertyName);
		}
		if (ValueUtil.isEmpty(value)) {
			value = getPropertyFromFile(propertyName);
		}

		return ValueUtil.isNotEmpty(value) ? value : defaultValue;
	}

	public static String getPropertyFromFile(String propertyName) {
		try {
			return CommonService.getPropertyService().getProperty(propertyName);
		} catch (NoSuchMessageException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getPropertyFromDB(String propertyName) {
		// TODO DB에서 Property 추출 로직 추가
		return null;
	}
}