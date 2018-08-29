package com.noar.stomp.client;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

public abstract class AbstractStompMessageHandler implements StompFrameHandler {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return Object.class;
	}
}