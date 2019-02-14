package com.noar.core.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * JSNOP에 대한 Controller Advice
 */
@ControllerAdvice
public class JsonPHandlerAspect extends AbstractJsonpResponseBodyAdvice {
	public JsonPHandlerAspect() {
		super("callback");
	}
}