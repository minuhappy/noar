package com.noar.stomp.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.noar.stomp.ConfigConstants;
import com.noar.stomp.Constants;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Autowired
	Environment env;

	@Override
	public boolean configureMessageConverters(List<MessageConverter> converters) {
		converters.add(new StringMessageConverter());
		return false;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		String destinationPrefixes = env.getProperty(ConfigConstants.STOMP_DESTINATION_PREFIXES, Constants.DEFAULT_STOMP_DEFAULT_BROKER);
		config.enableSimpleBroker(destinationPrefixes);
		// config.setApplicationDestinationPrefixes("/elidom/stomp");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// registry.addEndpoint(env.getProperty(ConfigConstants.STOMP_END_POINT_PATH, Constants.DEFAULT_STOMP_SERVER_ENDPOINT)).setAllowedOrigins("*").withSockJS();
		registry.addEndpoint(env.getProperty(ConfigConstants.STOMP_END_POINT_PATH, Constants.DEFAULT_STOMP_SERVER_ENDPOINT)).setAllowedOrigins("*");
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		int messageLimitSize = env.getProperty(ConfigConstants.STOMP_SEND_MESSAGE_LIMIT_SIZE, Integer.class, 2 * 1024 * 1024);
		int sendBufferLimitSize = env.getProperty(ConfigConstants.STOMP_BUFFER_LIMIT_SIZE, Integer.class, 5 * 1024 * 1024);
		int sendTimeLimit = env.getProperty(ConfigConstants.STOMP_SEND_TIME_LIMIT_MS, Integer.class, 0);

		registration.setMessageSizeLimit(messageLimitSize);
		registration.setSendBufferSizeLimit(sendBufferLimitSize);
		if (sendTimeLimit > 0)
			registration.setSendTimeLimit(sendTimeLimit);
	}
}