package com.noar.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * com.samsung.gmes2.base.util
 * MonitorUtil.java
 * 
 * 클래스 개요 : 동시성을 제어하는 기능을 합니다.
 * </pre>
 */
public class SynchCtrlUtil {
	private static final Logger logger = LoggerFactory.getLogger(SynchCtrlUtil.class);

	public static <T> T wrap(String name, IScope<T> closure) throws Throwable {
		synchronized (get(name)) {
			try {
				return closure.execute();
			} finally {
				release(name);
			}
		}
	}

	public static <K, T> T wrap(final String name, final Map<K, T> cache, final K key, final IScope<T> scope) throws Throwable {
		ValueUtil.assertNotEmpty("name", name);
//		ValueUtil.assertNotEmpty("cache", cache);

		final boolean debug = logger.isDebugEnabled();

		if (key == null) {
			if (debug)
				logger.debug("key is null, so execute closure right away.");
			return wrap(name, scope);
		}

		if (cache.containsKey(key)) {
			if (debug)
				logger.debug("get data from map cache by key: " + key);
			return cache.get(key);
		}

		return wrap(name, new IScope<T>() {
			public T execute() throws Throwable {
				if (cache.containsKey(key)) {
					if (debug)
						logger.debug("get data from map cache by key: " + key);
					return cache.get(key);
				}

				if (debug)
					logger.debug("get data by executing closure.");
				T value = scope.execute();
				if (value != null) {
					if (debug)
						logger.debug("put data to map cache by key: " + key);
					cache.put(key, value);
				}
				return value;
			}
		});
	}

	private static Map<String, Monitor> cache = new ConcurrentHashMap<String, Monitor>();

	public static Object get(String name) {
		ValueUtil.assertNotEmpty("name", name);

		final boolean debug = logger.isDebugEnabled();

		synchronized (cache) {
			Monitor monitor = null;
			try {
				if (cache.containsKey(name)) {
					monitor = cache.get(name);
					monitor.i++;
				} else {
					monitor = Monitor.newInstance();
					cache.put(name, monitor);
				}
			} finally {
				if (debug)
					logger.debug("get monitor name: " + name + ", index: " + monitor.i);
			}
			return monitor;
		}
	}

	public static void release(String name) {
		ValueUtil.assertNotEmpty("name", name);

		final boolean debug = logger.isDebugEnabled();

		synchronized (cache) {
			if (!cache.containsKey(name)) {
				if (debug)
					logger.debug("the monitor was not released because there was not any monitor with name: " + name);
				return;
			}

			Monitor monitor = cache.get(name);
			if (monitor.i > 0) {
				if (debug)
					logger.debug("release monitor name: " + name + ", index: " + monitor.i);
				monitor.i--;
				return;
			}

			if (debug)
				logger.debug("remove monitor name: " + name + ", index: " + monitor.i);
			cache.remove(name);
		}
	}

	static class Monitor {
		static Monitor newInstance() {
			return new Monitor();
		}

		int i = 0;
	}
}
