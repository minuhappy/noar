package com.noar.common.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadUtil {

	private static ThreadLocal<Map<String, Object>> PROPERTY_CACHE = new ThreadLocal<Map<String, Object>>();

	/**
	 * ThreadLocal에서 Property Map 가져오기 실행.
	 * 
	 * @return
	 */
	private static Map<String, Object> getMap() {
		if (PROPERTY_CACHE.get() == null)
			PROPERTY_CACHE.set(new HashMap<String, Object>());

		return PROPERTY_CACHE.get();
	}

	/**
	 * Thread Local Map에 Property 추가.
	 * 
	 * @param key
	 * @param value
	 */
	protected static void put(String key, Object value) {
		getMap().put(key, value);
	}

	/**
	 * Thread Local Map에 여러개의 Properties 추가.
	 * 
	 * @param properties
	 */
	protected static void putAll(Map<String, Object> properties) {
		if (properties == null)
			return;

		getMap().putAll(properties);
	}

	/**
	 * Thread Local Map에서 key에 해당하는 값 가져오기 실행.
	 * 
	 * @param key
	 * @return
	 */
	public static Object getProperty(String key) {
		Map<String, Object> map = PROPERTY_CACHE.get();
		return map == null ? null : map.get(key);
	}

	/**
	 * Thread Local Map에서 key 해당하는 값 제거.
	 * 
	 * @param key
	 */
	public static void remove(String key) {
		getMap().remove(key);
	}

	/**
	 * Thread Local의 Property Map 제거.
	 */
	public static void removeAll() {
		PROPERTY_CACHE.remove();
	}

	/**
	 * 동기 방식의 스레드 실행.
	 * 
	 * @param runnable
	 */
	public static String doScope(IScope<Object> scope) {
		return doScope(scope, null);
	}

	public static String doScope(IScope<Object> scope, Map<String, Object> properties) {
		return new SimpleThread(() -> {
			try {
				scope.execute();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}).start(properties);
	}

	protected static String doScope(Runnable runnable, Map<String, Object> properties) {
		return new SimpleThread(runnable).start(properties);
	}

	/**
	 * 비동기 방식의 스레드 실행.
	 * 
	 * @param runnable
	 */
	public static void doAsynch(IScope<Object> scope) {
		doAsynch(scope, null);
	}

	public static void doAsynch(IScope<Object> scope, Map<String, Object> properties) {
		new AsynchThread(() -> {
			try {
				scope.execute();
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}).start(properties);
	}

	/**
	 * Tread Sleep
	 * 
	 * @param sleepTime
	 */
	public static void sleep(int sleepTime) {
		sleep(ValueUtil.toLong(sleepTime));
	}

	public static void sleep(long sleepTime) {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

/**
 * 동기 방식의 스레드 클래스.
 * 
 * @author minu
 */
class SimpleThread {
	Runnable runnable;

	public SimpleThread(Runnable runnable) {
		this.runnable = runnable;
	}

	public String start(Map<String, Object> properties) {
		double start = System.currentTimeMillis();
		ThreadUtil.putAll(properties);
		runnable.run();
		ThreadUtil.removeAll();
		return ValueUtil.toString(System.currentTimeMillis() - start);
	}
}

/**
 * 비동기 방식의 스레드 클레스.
 * 
 * @author minu
 */
class AsynchThread implements Runnable {
	Runnable runnable;
	Map<String, Object> properties;

	public AsynchThread(Runnable runnable) {
		this.runnable = runnable;
	}

	public void start(Map<String, Object> properties) {
		this.properties = properties;

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		ThreadUtil.doScope(this.runnable, this.properties);
	}
}