/**
 * 
 */
package com.minu.core.util;

import com.minu.core.system.ConfigConstants;

import net.sf.common.util.BeanUtils;

/**
 * @author Minu.Kim
 *
 */
public class BeanUtil {
	private static BeanUtils beanUtils;

	private static BeanUtils getBeanUtils() {
		if (beanUtils == null) {
			beanUtils = BeanUtils.getInstance(ConfigConstants.APP_CONTEXT_NAME);
		}
		return beanUtils;
	}

	public static Object get(String beanName) {
		return getBeanUtils().get(beanName);
	}

	public static <T> T get(Class<T> requiredType) {
		return getBeanUtils().get(requiredType);
	}

	public static <T> T get(String beanName, Class<T> requiredType) {
		T bean = getBeanUtils().get(beanName, requiredType);
		return bean;
	}
}