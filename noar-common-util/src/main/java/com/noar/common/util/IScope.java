package com.noar.common.util;

import java.util.Map;

public interface IScope<T> {
	public T execute() throws Throwable;

	default void put(String key, Object value) {
		ThreadUtil.put(key, value);
	}

	default void putAll(Map<String, Object> properties) {
		ThreadUtil.putAll(properties);
	}

	default void remove(String key) {
		ThreadUtil.remove(key);
	}

	default void removeAll() {
		ThreadUtil.removeAll();
	}
}
