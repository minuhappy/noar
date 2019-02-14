package com.noar.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Minu.Kim
 */
@SuppressWarnings("all")
public class ThreadPropertyUtil {
	public static Object doScope(IScope scope) throws Throwable {
		boolean wasRemoveAllEnabled = isRemoveAllEnabled();
		if (wasRemoveAllEnabled) {
			setRemoveAllEnabled(false);
		}
		try {
			return scope.execute();
		} finally {
			if (wasRemoveAllEnabled) {
				removeAll();
			}
		}
	}

	/**
	 * 쓰레드 속성값을 조회합니다. 이 값은 현재의 쓰레드에서만 유효한 값입니다.
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return getMap().get(key);
	}

	/**
	 * 쓰레드 속성값을 설정합니다. 이 값은 현재의 쓰레드에서만 유효한 값입니다.
	 * 
	 * @param key
	 * @param value
	 */
	public static void put(String key, Object value) {
		getMap().put(key, value);
	}

	/**
	 * 쓰레드 속성값이 있는지를 확인합니다.
	 * 
	 * @param key
	 * @return
	 */
	public static boolean contains(String key) {
		return getMap().containsKey(key);
	}

	/**
	 * 쓰레드 속성값을 파기합니다.
	 * 
	 * @param key
	 * @return
	 */
	public static Object remove(String key) {
		return getMap().remove(key);
	}

	private static final String PROP_REMOVEALL_ENABLED = "ThreadPropertyUtil.removeAll.enabled";

	private static boolean isRemoveAllEnabled() {
		return ValueUtil.toBoolean(get(PROP_REMOVEALL_ENABLED), true);
	}

	private static void setRemoveAllEnabled(boolean enabled) {
		put(PROP_REMOVEALL_ENABLED, enabled);
	}

	private static Map<Long, Map<String, Object>> cache = new ConcurrentHashMap<Long, Map<String, Object>>();

	private static Map<String, Object> getMap() {
		Long threadId = Thread.currentThread().getId();
		if (cache.containsKey(threadId)) {
			return cache.get(threadId);
		}
		Map<String, Object> propMap = new HashMap<String, Object>();
		cache.put(threadId, propMap);
		return propMap;
	}

	/**
	 * 쓰레드 속성값 전체를 파기합니다.
	 */
	private static void removeAll() {
		cache.remove(Thread.currentThread().getId());
	}
}