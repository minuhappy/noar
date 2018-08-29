package com.noar.stomp.server;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.noar.stomp.Constants;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public boolean configureMessageConverters(List<MessageConverter> converters) {
		converters.add(new StringMessageConverter());
		return false;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(Constants.DEFAULT_STOMP_SERVER_ENDPOINT).setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setMessageSizeLimit(2 * 1024 * 1024);
		registration.setSendBufferSizeLimit(5 * 1024 * 1024);
		// registration.setSendTimeLimit(10000);
	}
}