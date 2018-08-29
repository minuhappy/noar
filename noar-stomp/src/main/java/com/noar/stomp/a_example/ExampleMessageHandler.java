package com.noar.stomp.a_example;

import org.springframework.messaging.simp.stomp.StompHeaders;

import com.noar.stomp.client.AbstractStompMessageHandler;

public class ExampleMessageHandler extends AbstractStompMessageHandler {
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		String value = new String((byte[]) payload);
		this.logger.info("DefaultStompHandler received this message \n" + value);
	}
}