package com.noar.common.util.config.property;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import com.noar.common.util.config.property.impl.PropertyServiceImpl;

/*
 * Common Service provides interfaces for User application.
 * Every single member of this classes must be injected.
 */
public class CommonService {

	private static final String DEFAULT_PROPERTY_GROUP_NAME = "propertyService";

	private static PropertyService propertyService;

	public static PropertyService getPropertyService() {
		if (propertyService == null) {
			initCommonService();
		}
		return propertyService;
	}

	private static void initCommonService() {
		CommonService.propertyService = propertyService();
	}

	private static PropertyServiceImpl propertyService() {
		MessageSource propertiesSource = propertiesSource();

		Map<String, MessageSource> paramMap = new HashMap<String, MessageSource>();
		paramMap.put(DEFAULT_PROPERTY_GROUP_NAME, propertiesSource);

		PropertyServiceImpl propertyService = new PropertyServiceImpl();
		propertyService.setPropertyResources(paramMap);
		propertyService.setDefaultPropertyGroupName(DEFAULT_PROPERTY_GROUP_NAME);

		return propertyService;
	}

	private static ReloadableResourceBundleMessageSource propertiesSource() {
		String fileNames = "core, base";
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
}
