package com.noar.core.sample.service.api;

import org.apache.commons.lang.ClassUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RestController;

public class SpringApiSampleBiz {
	// http://127.0.0.1:8080/sample/service/api/spring-api-sample/class-path-scanning-candidate-component-provider
	public String classPathScanningCandidateComponentProvider(String value) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

		for (BeanDefinition bd : scanner.findCandidateComponents("com.minu.core")) {
			String beanName = bd.getBeanClassName();
			try {
				Class<?> entityClass = ClassUtils.getClass(beanName);
				System.out.println(entityClass.getName());
			} catch (ClassNotFoundException e) {
				System.out.println("DD");
			}
		}

		return "Test";
	}
}