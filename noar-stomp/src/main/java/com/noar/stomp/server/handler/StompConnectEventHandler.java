package com.noar.stomp.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Component;

/**
 * Stomp Server Start & Shutdown Envent.
 */
@Component
public class StompConnectEventHandler implements ApplicationListener<BrokerAvailabilityEvent> {
	/**
	 * Logger
	 */
	private Logger logger = LoggerFactory.getLogger(StompConnectEventHandler.class);

	/**
	 * Stomp Server Start & Shutdown Envent.
	 */
	public void onApplicationEvent(BrokerAvailabilityEvent event) {
		this.logger.debug("Stomp Broker Available : " + event.isBrokerAvailable());
	}
}
