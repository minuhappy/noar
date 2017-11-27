package com.minu.core.sample.pubsub;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

	@MessageMapping("/hello")
	@SendTo("/noar/topic/greetings")
	public Map<String, Object> hello(Map<String, Object> message) throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.putAll(message);
		return returnMap;
	}
}