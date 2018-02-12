package com.noar.core.system.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.noar.common.util.BeanUtil;
import com.noar.common.util.IScope;
import com.noar.common.util.ThreadPropertyUtil;
import com.noar.core.exception.ServerException;
import com.noar.core.system.Constants;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.system.handler.RestServiceHandler;
import com.noar.core.util.TransactionUtil;
import com.noar.core.util.ValueUtil;

import net.sf.common.util.Closure;

@RestController
public class EntityRestController {
	@RequestMapping(value = "**/entity/**/*", headers = "Accept=application/json;charset=UTF-8")
	public @ResponseBody Object restService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		return ThreadPropertyUtil.doScope(new IScope() {
			@Override
			public Object execute() throws Throwable {
				// Controller 앞 단에 실행 될 수 있도록 수정.
				ThreadPropertyUtil.put(Constants.HTTP_REQUEST, req);
				return doRestService(req, res);
			}
		});
	}

	private Object doRestService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		RestServiceHandler restServiceHandler = BeanUtil.get(RestServiceHandler.class);
		ServiceInfo serviceInfo = restServiceHandler.get(req);

		Object restParam = ThreadPropertyUtil.get(Constants.REST_PARAM);
		String inputPram = ValueUtil.isNotEmpty(restParam) ? String.valueOf(restParam) : null;
		if (ValueUtil.isEmpty(inputPram)) {
			inputPram = getInputJsonParam(req);
		}

		final String restInput = inputPram;
		return TransactionUtil.doScope("RestServiceUtil.Invoke", new Closure<Object, Throwable>() {
			@Override
			public Object execute() throws Throwable {
				return restServiceHandler.invoke(serviceInfo, req.getMethod(), restInput);
			}
		});
	}

	/**
	 * Request에서 전송된 Json Prameter를 추출.
	 * 
	 * @param request
	 * @return
	 */
	private String getInputJsonParam(HttpServletRequest request) {
		StringBuffer sb = null;

		try {
			sb = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			throw new ServerException("Failed to read request body!", e);
		}

		return sb.toString();
	}
}