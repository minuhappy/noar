package com.minu.core.property;


/*
 * Common Service provides interfaces for User application.
 * Every single member of this classes must be injected.
 */
public class CommonService {

	private static PropertyService propertyService;
	
	public static PropertyService getPropertyService() {
		return propertyService;
	}
	public void setPropertyService(PropertyService propertyService) {
		CommonService.propertyService = propertyService;
	}
}
