/* Copyright © HatioLab Inc. All rights reserved. */
package com.noar.stomp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Component;

/**
 * 기본 Stomp 메시지 핸들러 
 * 
 * @author shortstop
 */
@Component
public class DefaultStompMessageHandler extends AbstractStompMessageHandler {
	
	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(DefaultStompMessageHandler.class);

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		String value = new String((byte[]) payload);
		this.logger.info("DefaultStompHandler received this message \n" + value);
	}
}