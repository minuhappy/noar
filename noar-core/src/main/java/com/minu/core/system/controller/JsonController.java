package com.minu.core.system.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.minu.core.exception.ServerException;
import com.minu.core.system.Constants;
import com.minu.core.system.base.IScope;
import com.minu.core.system.base.ServiceInfo;
import com.minu.core.system.handler.JsonServiceHandler;
import com.minu.core.util.BeanUtil;
import com.minu.core.util.ThreadPropertyUtil;

@RestController
public class JsonController {
	@RequestMapping(value = "**/service/**/*", headers = "Accept=application/json;charset=UTF-8")
	public @ResponseBody Object jsonService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		return ThreadPropertyUtil.doScope(new IScope() {
			@Override
			public Object execute() throws Throwable {
				// Controller 앞 단에 실행 될 수 있도록 수정.
				ThreadPropertyUtil.put(Constants.HTTP_REQUEST, req);
				return doJsonService(req, res);
			}
		});
	}
	
	private Object doJsonService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		JsonServiceHandler jsonService = BeanUtil.get(JsonServiceHandler.class);
		ServiceInfo serviceInfo = jsonService.get(req.getRequestURI());
		String inputPram = this.getInputJsonParam(req);
		return jsonService.invoke(serviceInfo, inputPram);
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