package com.noar.core.system.controller;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.noar.common.util.BeanUtil;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.system.handler.JsonServiceHandler;

/**
 * WebSocket Controller
 * 
 * @author Minu
 */
@ServerEndpoint("/service/{service}/{methods}")
public class WebSocketController {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

	/**
	 * WebSocket Event 처리를 위한 Controller
	 * 
	 * @sample ws://localhost:8080/core/service/blank/sample/list
	 * @param session
	 * @param message
	 * @return
	 * @throws Throwable
	 */
	@OnMessage
	public Object handleMessage(@PathParam("module") String module, Session session, String message) {
		String url = session.getRequestURI().getPath();

		try {
			JsonServiceHandler jsonService = BeanUtil.get(JsonServiceHandler.class);
			ServiceInfo serviceInfo = jsonService.get(url);
			return jsonService.invoke(serviceInfo, message);
		} catch (Throwable e) {
			logger.error("Error", e);
			return e.getMessage();
		}
	}
}