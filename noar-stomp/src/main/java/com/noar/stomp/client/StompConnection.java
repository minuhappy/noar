package com.noar.stomp.client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
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

/**
 * Stomp server에 접속 Session을 관리하는 Connection 객체
 * 기본 subject session과 subject별 session을 관리
 */
@Service
public class StompConnection {

	/**
	 * Logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(StompConnection.class);

	/**
	 * Stomp Session Map : key - subject, value - Stomp Session
	 */
	private Map<String, DefaultStompSession> STOMP_SESSION_MANAGER = new ConcurrentHashMap<String, DefaultStompSession>();

	/**
	 * Connect to stomp server
	 * 
	 * @param host
	 * @param port
	 * @param stompSessionHandler
	 * @return
	 */
	public DefaultStompSession connect(String host, int port, String endPoint, StompSessionHandler stompSessionHandler) {
		WebSocketTransport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		RestTemplateXhrTransport restTemplateXhrTransport = new RestTemplateXhrTransport(new RestTemplate());
		List<Transport> transports = Arrays.asList(webSocketTransport, restTemplateXhrTransport);

		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		stompClient.setMessageConverter(new SimpleMessageConverter());

		String hostUrl = new StringBuilder().append("http://").append(host).append(":{port}").append(endPoint).toString();
		ListenableFuture<StompSession> listenableFuture = stompClient.connect(hostUrl.toString(), stompSessionHandler, port);

		DefaultStompSession stompSession = null;

		try {
			stompSession = (DefaultStompSession) listenableFuture.get();
			logger.info("Connected Stomp Client [" + host + ":" + port + endPoint + "].");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return stompSession;
	}

	/**
	 * Get Stomp Session Handler
	 * 
	 * @return
	 */
	public StompSessionHandler getSessionHandler(final String subject, final StompFrameHandler stompFrameHandler) {
		return new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				try {
					STOMP_SESSION_MANAGER.put(subject, (DefaultStompSession) session);
					session.subscribe(subject, stompFrameHandler);
				} catch (Exception e) {
					throw new RuntimeException("Failed to subscribe stomp session", e);
				}
			}

			/**
			 * Stomp Connection Error Event
			 */
			@Override
			public void handleTransportError(StompSession session, Throwable exception) {
				if (exception instanceof ConnectionLostException) {
					String sessionId = session.getSessionId();
					Set<String> keys = STOMP_SESSION_MANAGER.keySet();
					for (String key : keys) {
						DefaultStompSession getStompSession = STOMP_SESSION_MANAGER.get(key);

						// Session ID를 통해 STOMP_SESSIOIN_MAP에서 StompSession 찾기 실행.
						if (!getStompSession.getSessionId().equals(sessionId)) {
							continue;
						}
					}

					// STOMP_SESSION_MAP 초기화
					STOMP_SESSION_MANAGER.clear();
					STOMP_SESSION_MANAGER = new ConcurrentHashMap<String, DefaultStompSession>();
				}
			}
		};
	}

	/**
	 * Stomp Session Map에서 Subject에 Mapping되는 Stomp Session 가져오기 실행.
	 * 
	 * @param subject
	 * @return
	 */
	public StompSession getStompSession(String subject) {
		return this.STOMP_SESSION_MANAGER.get(subject);
	}

	/**
	 * Stomp Session Map에서 subject에 Mapping되는 Stomp Session 중단 및 제거.
	 * 
	 * @param subject
	 */
	public void removeStompSession(String subject) {
		DefaultStompSession stompSession = this.STOMP_SESSION_MANAGER.get(subject);
		this.disconnect(stompSession);
		this.STOMP_SESSION_MANAGER.remove(subject);
	}

	/**
	 * Disconnect Stomp Server
	 * 
	 * @param stompSession
	 * @return
	 */
	private boolean disconnect(DefaultStompSession stompSession) {
		stompSession.disconnect();
		return true;
	}
}