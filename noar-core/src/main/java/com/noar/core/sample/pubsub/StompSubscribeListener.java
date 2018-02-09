package com.noar.core.sample.pubsub;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContextEvent;

import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Service
public class StompSubscribeListener {
	public void subscribe(ServletContextEvent sce) {
		WebSocketTransport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		RestTemplateXhrTransport restTemplateXhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		List<Transport> transports = Arrays.asList(webSocketTransport, restTemplateXhrTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		StompSessionHandler handler = new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				session.subscribe("/noar/topic/greetings", new StompFrameHandler() {

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						String value = new String((byte[]) payload);
						System.out.println(value);
					}

					@Override
					public Type getPayloadType(StompHeaders headers) {
						return Object.class;
					}
				});
			}
		};

		stompClient.setAutoStartup(true);
		stompClient.setMessageConverter(new SimpleMessageConverter());
		stompClient.connect("http://localhost:{port}/noar", handler, "8080");
	}
}