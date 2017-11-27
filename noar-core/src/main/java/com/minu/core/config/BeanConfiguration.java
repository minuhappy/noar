package com.minu.core.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.minu.core.property.CommonService;
import com.minu.core.property.impl.PropertyServiceImpl;
import com.minu.core.system.ConfigConstants;
import com.minu.core.util.BeanUtil;

import net.sf.common.util.BeanUtils;

@Configuration
@ImportResource({ "classpath:/META-INF/*-context.xml" })
public class BeanConfiguration {
	@Resource
	private Environment env;

	@Bean
	public BeanUtils beanUtils() throws Exception {
		BeanUtils beanUtils = new BeanUtils();
		beanUtils.setApplicationContextName(ConfigConstants.APP_CONTEXT_NAME);
		return beanUtils;
	}

	@Bean
	public ReloadableResourceBundleMessageSource propertiesSource() throws Exception {
		String fileNames = env.getProperty(ConfigConstants.PROPERTY_FILE_NAMES, "core");
		String[] nameArray = StringUtils.tokenizeToStringArray(fileNames, ",");

		int size = nameArray.length + 1;

		String[] configArray = new String[size];
		configArray[0] = "classpath:/application";

		for (int i = 1; i < size; i++) {
			configArray[i] = "classpath:/properties/" + nameArray[i - 1];
		}

		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(configArray);
		messageSource.setCacheSeconds(-1);
		return messageSource;
	}

	@Bean
	@DependsOn("propertiesSource")
	public PropertyServiceImpl propertyService() throws Exception {
		MessageSource propertiesSource = BeanUtil.get(ReloadableResourceBundleMessageSource.class);
		Map<String, MessageSource> paramMap = new HashMap<String, MessageSource>();
		paramMap.put(ConfigConstants.DEFAULT_PROPERTY_GROUP_NAME, propertiesSource);

		PropertyServiceImpl propertyService = new PropertyServiceImpl();
		propertyService.setPropertyResources(paramMap);
		propertyService.setDefaultPropertyGroupName(ConfigConstants.DEFAULT_PROPERTY_GROUP_NAME);
		return propertyService;
	}

	@Bean
	@DependsOn("propertyService")
	public CommonService commonService() throws Exception {
		PropertyServiceImpl propertyService = BeanUtil.get(PropertyServiceImpl.class);
		CommonService commonService = new CommonService();
		commonService.setPropertyService(propertyService);
		return commonService;
	}
}