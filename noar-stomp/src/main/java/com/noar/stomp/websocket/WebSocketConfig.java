package com.noar.stomp.websocket;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.noar.stomp.ConfigConstants;

@Service
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

	@Resource
	private Environment env;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// registry.addHandler(webSocketHandler(), "/ws").setAllowedOrigins("*");
		// registry.addHandler(new MyHandler(), "/myHandler");
		// registry.addHandler(new MyHandler(), "/myHandler").addInterceptors(new HttpSessionHandshakeInterceptor());
	}

	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	/*
	 * @Bean
	 * public ElidomWebSocketHandler webSocketHandler() {
	 * return new ElidomWebSocketHandler();
	 * }
	 */

	/**
	 * Web Socket Message Size(Buffer) 설정
	 * 
	 * @return
	 */
	@Bean
	public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
		this.logger.info("WebSocket Configuration Starting ...");
		String MessageBufferSize = env.getProperty(ConfigConstants.WEBSOCKET_MAX_TEXT_MESSAGE_BUFFER_SIZE, "20242880");
		String BinaryBufferSize = env.getProperty(ConfigConstants.WEBSOCKET_MAX_BINARY_MESSAGE_BUFFER_SIZE, "20242880");

		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(Integer.parseInt(MessageBufferSize));
		container.setMaxBinaryMessageBufferSize(Integer.parseInt(BinaryBufferSize));
		this.logger.info("WebSocket Configuration Finished ...");
		return container;
	}
}