package com.noar.core.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noar.common.util.BeanUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.exception.ServiceException;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.system.converter.RestServiceConverter;
import com.noar.core.system.handler.RestEntityServiceHandler;
import com.noar.core.system.handler.RestTableServiceHandler;

@RestController
public class RestServiceController {

	@Autowired
	private Environment env;

	private Boolean enableTableService;

	@RequestMapping(value = "/rest/**/*", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Object restService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		return doRestService(req, res);
	}

	// @RequestMapping(value = "/rest/{name}/{id}/{details}/{detail_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	// public @ResponseBody Object restServiceDetail(HttpServletRequest req, HttpServletResponse res) throws Throwable {
	// return ThreadPropertyUtil.doScope(new IScope<Object>() {
	// @Override
	// public Object execute() throws Throwable {
	// // Controller 앞 단에 실행 될 수 있도록 수정.
	// ThreadPropertyUtil.put(Constants.HTTP_REQUEST, req);
	// return doRestService(req, res);
	// }
	// });
	// }

	private Object doRestService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		ServiceInfo serviceInfo = BeanUtil.get(RestServiceConverter.class).parseServiceInfo(req);

		if (serviceInfo.getBean() != null)
			return BeanUtil.get(RestEntityServiceHandler.class).invoke(serviceInfo);

		if (enableTableService())
			return BeanUtil.get(RestTableServiceHandler.class).invoke(serviceInfo);

		throw new ServiceException("Invalide Service Name : " + serviceInfo.getName());
	}

	private boolean enableTableService() {
		if (this.enableTableService == null)
			this.enableTableService = env.getProperty(ConfigConstants.REST_TABLE_SERVICE_ENABLED, Boolean.class, true);
		return this.enableTableService;
	}
}