package com.noar.stomp.z_example.stomp;

import org.springframework.messaging.simp.stomp.StompHeaders;

import com.noar.stomp.client.AbstractStompMessageHandler;

public class ExampleMessageHandler extends AbstractStompMessageHandler {
	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		String value = new String((byte[]) payload);
		this.logger.info("DefaultStompHandler received this message \n" + value);
	}
}