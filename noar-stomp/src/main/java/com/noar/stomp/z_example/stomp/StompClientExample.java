package com.noar.stomp.z_example.stomp;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.DefaultStompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;

import com.noar.stomp.Constants;
import com.noar.stomp.client.StompConnection;

@Service
public class StompClientExample {

	@Autowired
	StompConnection stompConnection;

	public void example() {
		String host = "127.0.0.1";
		int port = 8080;

		String subject = "/sample";

		StompSessionHandler stompSessionHandler = stompConnection.getSessionHandler(subject, new ExampleMessageHandler());
		DefaultStompSession session = stompConnection.connect(host, port, Constants.DEFAULT_STOMP_SERVER_ENDPOINT, stompSessionHandler);

		session.send(subject, "Success".getBytes(Charset.forName("UTF-8")));
	}
}
