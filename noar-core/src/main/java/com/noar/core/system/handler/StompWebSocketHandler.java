package com.noar.core.system.handler;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.DefaultStompSession;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import com.noar.core.system.ConfigConstants;

@Service
public class StompWebSocketHandler implements Runnable {

	@Resource
	private Environment env;

	private DefaultStompSession defaultStompSession;

	public DefaultStompSession getDefaultStompSession() {
		return defaultStompSession;
	}

	public void setDefaultStompSession(DefaultStompSession defaultStompSession) {
		this.defaultStompSession = defaultStompSession;
	}

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(StompWebSocketHandler.class);

	@EventListener({ ContextRefreshedEvent.class })
	public void start() {
		Thread stompSubscriber = new Thread(this);
		stompSubscriber.start();
	}

	@EventListener({ ContextClosedEvent.class })
	public void close() {
		this.disConnect(this.defaultStompSession);
	}

	/**
	 * Stomp Session Handler
	 * 
	 * @return
	 */
	public StompSessionHandler getStompSessionHandler() {
		return new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				session.subscribe("/noar/topic/sample", new StompFrameHandler() {
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
	}

	/**
	 * Stomp Connection Thread 실행.
	 */
	@Override
	public void run() {
		DefaultStompSession stompSession = null;

		while (true) {
			try {
				// Server가 Start Up 되기전에 실행 될 수 있으므로, 10초간 Sleep Time 지정.
				Thread.sleep(10000);

				if (stompSession == null || !stompSession.isConnected()) {
					// Server 연결 정보 추출
					String host = env.getProperty(ConfigConstants.STOMP_SERVER_HOST, "http://localhost:{port}/noar");
					String port = env.getProperty(ConfigConstants.STOMP_SERVER_PORT, "8080");

					stompSession = connect(host, port, this.getStompSessionHandler());
					setDefaultStompSession(stompSession);

					logger.info("Connected Stomp Client.");
				}
			} catch (Exception e) {
				logger.info("StompSubscriber error : " + e.getMessage());
				continue;
			}
		}
	}

	/**
	 * Connect Stomp Server
	 * 
	 * @param host
	 * @param port
	 * @param stompSessionHandler
	 * @return
	 * @throws Exception
	 */
	public DefaultStompSession connect(String host, String port, StompSessionHandler stompSessionHandler) throws Exception {
		DefaultStompSession stompSession = null;

		WebSocketTransport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		RestTemplateXhrTransport restTemplateXhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		List<Transport> transports = Arrays.asList(webSocketTransport, restTemplateXhrTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new SimpleMessageConverter());

		ListenableFuture<StompSession> listenableFuture = stompClient.connect(host, stompSessionHandler, port);

		try {
			stompSession = (DefaultStompSession) listenableFuture.get();
		} catch (Exception e) {
			throw e;
		}
		return stompSession;
	}

	/**
	 * Disconnect Stomp Server
	 * 
	 * @param stompSession
	 * @return
	 */
	public boolean disConnect(DefaultStompSession stompSession) {
		stompSession.disconnect();
		return true;
	}

	/**
	 * Send
	 * 
	 * @param destination
	 * @param message
	 */
	public void send(String destination, String message) {
		// DefaultStompSession defaultStompSession = BeanUtil.get(StompWebSocketHandler.class).getDefaultStompSession();
		this.defaultStompSession.send(destination, message.getBytes());
	}
}