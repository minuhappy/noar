package com.noar.core.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.noar.common.util.BeanUtil;
import com.noar.common.util.IScope;
import com.noar.common.util.ThreadPropertyUtil;
import com.noar.core.Constants;
import com.noar.core.system.handler.EntityServiceHandler;
import com.noar.core.util.TransactionUtil;

@RestController
public class EntityRestController {
	@RequestMapping(value = "**/entity/**/*", headers = "Accept=application/json;charset=UTF-8")
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

	private Object doRestService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		return TransactionUtil.doScope("RestServiceUtil.Invoke", new IScope<Object>() {
			@Override
			public Object execute() throws Throwable {
				return BeanUtil.get(EntityServiceHandler.class).invoke(req);
			}
		});
	}
}