package com.noar.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <pre>
 * 
 * 클래스 개요 : 동시성을 제어하는 기능을 합니다.
 * </pre>
 */
public class SynchCtrlUtil {
	public static Object doScope(String name, IScope scope) {
		synchronized (get(name)) {
			try {
				return execute(scope);
			} finally {
				remove(name);
			}
		}
	}

	public static <K, V> V doScope(final String name, final Map<K, V> cache, final K key, final IScope scope) {
		return doScope(name, cache, key, false, scope);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> V doScope(final String name, final Map<K, V> cache, final K key, final boolean doClone, final IScope scope) {
		/**
		 * MapCache 이용 시
		 */

		if (cache == null) {
			throw new RuntimeException("cache is null.");
		} else if (key == null) {
			return null;
		}
		if (cache.containsKey(key)) {
			return adjustData(cache.get(key), doClone);
		}
		return (V) doScope(name, () -> {
			if (cache.containsKey(key))
				return adjustData(cache.get(key), doClone);

			V data = (V) SynchCtrlUtil.execute(scope);
			if (data != null)
				cache.put(key, data);

			return adjustData(data, doClone);
		});
	}

	private static Object execute(IScope scope) {
		try {
			return scope.execute();
		} catch (Throwable t) {
			throw new RuntimeException(t);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T adjustData(T data, boolean doClone) {
		if (data == null || !doClone) {
			return data;
		} else if (data instanceof List) {
			return (T) new ArrayList<Object>((List<?>) data);
		}
		return data;
	}

	private static Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
	private static Map<String, Integer> sizeCache = new ConcurrentHashMap<String, Integer>();

	/**
	 * synchronized 영역을 위한 명명된 모니터 객체를 반환합니다.<br>
	 * 아래와 같은 형식으로 사용합니다. <code>
	 * <pre>
	 * synchronized (getMonitoer(name)) {
	 * 	try {
	 * 		...
	 * 	} finally {
	 * 		expireMonitor(name);
	 * 	}
	 * }
	 * </pre>
	 * </code>
	 * 
	 * @param name
	 * @return
	 */
	private static Object get(String name) {
		if (name == null)
			ValueUtil.assertNotEmpty("Name", name);

		synchronized (cache) {
			if (cache.containsKey(name)) {
				int size = sizeCache.get(name) + 1;
				sizeCache.put(name, size);
				return cache.get(name);
			}
			
			Object obj = new Object();
			sizeCache.put(name, 0);
			cache.put(name, obj);
			return obj;
		}
	}

	/**
	 * 명명된 모니터 객체를 제거합니다.
	 * 
	 * @param name
	 */
	private static void remove(String name) {
		if (name == null)
			ValueUtil.assertNotEmpty("Name", name);

		synchronized (cache) {
			if (sizeCache.containsKey(name)) {
				int size = sizeCache.get(name);
				if (size > 0) {
					size--;
					sizeCache.put(name, size);
				} else {
					cache.remove(name);
					sizeCache.remove(name);
				}
			} else if (cache.containsKey(name)) {
				cache.remove(name);
			}
		}
	}
}