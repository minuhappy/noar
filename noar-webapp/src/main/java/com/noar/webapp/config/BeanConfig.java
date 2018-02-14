package com.noar.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.noar.common.util.BeanUtil;

@Service
public class BeanConfig {
	/**
	 * Bean Util Initialize
	 * @return
	 * @throws Exception
	 */
	@Bean
	public BeanUtil beanUtil() throws Exception {
		return new BeanUtil();
	}
}
