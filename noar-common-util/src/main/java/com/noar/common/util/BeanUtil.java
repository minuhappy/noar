package com.noar.common.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class BeanUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	/**
	 * Register Bean
	 * 
	 * @param clazz
	 */
	private static void register(Class<?> clazz) {
		register(clazz.getName(), clazz);
	}

	public static void register(String beanName, Class<?> clazz) {
		BeanDefinition bd = newBeanDefinition(clazz);
		register(beanName, bd);
	}

	public static void register(String beanName, BeanDefinition bd) {
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((AbstractApplicationContext) applicationContext).getBeanFactory();
		beanFactory.registerBeanDefinition(beanName, bd);
	}

	private static BeanDefinition newBeanDefinition(Class<?> clazz) {
		BeanDefinition bd;
		if (applicationContext instanceof GenericApplicationContext) {
			bd = BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition();
		} else if (applicationContext instanceof AbstractRefreshableApplicationContext) {
			bd = BeanDefinitionBuilder.rootBeanDefinition(clazz).getBeanDefinition();
		} else {
			throw new RuntimeException("Unsupported applicationContext type: " + applicationContext.getClass().getName());
		}
		return bd;
	}

	/**
	 * Get Bean By Name
	 * 
	 * @param name
	 * @return
	 */
	public static Object get(final String name) {
		return applicationContext.getBean(name);
	}

	/**
	 * Get Bean by name and type
	 * 
	 * @param <T>
	 * @param name
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String name, Class<T> requiredType) {
		Object obj = get(name);
		if (requiredType != null && !requiredType.isAssignableFrom(obj.getClass())) {
			throw new BeanNotOfRequiredTypeException(name, requiredType, obj.getClass());
		}
		return (T) obj;
	}

	public static <T> T get(final Class<T> clazz) {
		return get(clazz, clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(final Class<?> clazz, final Class<T> requiredType) {
		if (requiredType == null) {
			throw new RuntimeException("requiredType is null.");
		}

		T bean;
		String className = clazz.getName();
		try {
			bean = (T) applicationContext.getBean(clazz);
		}
		// NoSuchBeanDefinitionException �Ǵ� BeanNotOfRequiredTypeException �߻�
		catch (BeansException e) {
			try {
				bean = applicationContext.getBean(className, requiredType);
			} catch (NoSuchBeanDefinitionException e1) {
				register(clazz);
				bean = applicationContext.getBean(className, requiredType);
			}
		}
		return bean;
	}

	/**
	 * ApplicationContext Injection
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return BeanUtil.applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BeanUtil.applicationContext = applicationContext;
	}
}