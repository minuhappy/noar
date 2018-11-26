package com.noar.core.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.noar.common.util.BeanUtil;
import com.noar.common.util.IScope;
import com.noar.common.util.ThreadPropertyUtil;
import com.noar.core.Constants;
import com.noar.core.system.handler.RestServiceHandler;
import com.noar.core.util.TransactionUtil;

@RestController
public class RestServiceController {
	@RequestMapping(value = "/rest/**/*", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Object restService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		return ThreadPropertyUtil.doScope(new IScope<Object>() {
			@Override
			public Object execute() throws Throwable {
				// Controller 앞 단에 실행 될 수 있도록 수정.
				ThreadPropertyUtil.put(Constants.HTTP_REQUEST, req);
				return doRestService(req, res);
			}
		});
	}
	
//	@RequestMapping(value = "/rest/{name}/{id}/{details}/{detail_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public @ResponseBody Object restServiceDetail(HttpServletRequest req, HttpServletResponse res) throws Throwable {
//		return ThreadPropertyUtil.doScope(new IScope<Object>() {
//			@Override
//			public Object execute() throws Throwable {
//				// Controller 앞 단에 실행 될 수 있도록 수정.
//				ThreadPropertyUtil.put(Constants.HTTP_REQUEST, req);
//				return doRestService(req, res);
//			}
//		});
//	}

	private Object doRestService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		return TransactionUtil.doScope("RestServiceUtil.Invoke", new IScope<Object>() {
			@Override
			public Object execute() throws Throwable {
				return BeanUtil.get(RestServiceHandler.class).invoke(req);
			}
		});
	}
}