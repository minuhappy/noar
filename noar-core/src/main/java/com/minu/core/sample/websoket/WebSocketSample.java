package com.minu.core.sample.websoket;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;

@Service
@ServerEndpoint("/websocket/sample")
public class WebSocketSample {
	@OnMessage
	public boolean execute(String message) {
		System.out.println("DDDDDDDDDDDDDD");
		return true;
	}
}