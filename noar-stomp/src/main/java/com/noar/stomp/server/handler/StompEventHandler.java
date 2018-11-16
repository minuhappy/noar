package com.noar.stomp.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

public class StompEventHandler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@EventListener
	private void handleSessionConnected(SessionConnectedEvent event) {
		logger.debug("SessionConnectedEvent");
	}
	
	@EventListener
	private void handleSessionConnect(SessionConnectEvent event) {
		logger.debug("SessionConnectedEvent");
	}
	
	/**
	 * Session이 disconnect될 때 핸들러
	 * 
	 * @param event
	 */
	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		String reason = event.getCloseStatus().getReason();

		StringBuilder message = new StringBuilder();
		message.append("Session disconnected : sessionId {}");
		if (reason != null && !reason.isEmpty()) {
			message.append(", close reason : {}");
		}

		logger.info(message.toString(), sessionId, reason);
	}
	
	@EventListener
	private void handleSessionSubscribe(SessionSubscribeEvent event) {
		logger.debug("SessionSubscribeEvent");
	}
	
	@EventListener
	private void handleSessionUnsubscribe(SessionUnsubscribeEvent event) {
		logger.debug("SessionUnsubscribeEvent");
	}
}
