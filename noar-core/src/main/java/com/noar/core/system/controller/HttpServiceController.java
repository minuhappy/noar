package com.noar.core.system.controller;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.noar.common.util.BeanUtil;
import com.noar.core.exception.ServerException;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.system.handler.HttpServiceHandler;

@RestController
public class HttpServiceController {
	// @RequestMapping(value = "**/service/**/*", headers = "Accept=application/json;charset=UTF-8")
	@RequestMapping(value = "/service/**/*", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Object jsonService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		return doJsonService(req, res);
	}

	private Object doJsonService(HttpServletRequest req, HttpServletResponse res) throws Throwable {
		HttpServiceHandler jsonService = BeanUtil.get(HttpServiceHandler.class);
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