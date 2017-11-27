/**
 * 
 */
package com.minu.orm.processor;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 *
 */
@Service
public class VelocityProcessor {
	private VelocityEngine engine;

	public String process(String value, Map<String, Object> contextMap) {
		if (engine == null) {
			synchronized (this) {
				engine = new VelocityEngine();
				engine.init();
			}
		}

		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext(contextMap);
		engine.evaluate(context, writer, value, value);
		return writer.toString();
	}
}