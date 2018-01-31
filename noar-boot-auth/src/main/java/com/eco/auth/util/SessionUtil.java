package com.eco.auth.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Session 핸들링을 위한 유틸리티 클래스 
 * 
 * @author Minu.Kim
 */
public class SessionUtil {

	/**
	 * session 정보로 부터 attribute 값을 가져 오기 위한 method
	 * 
	 * @param name
	 * @return
	 */
	public static Object getAttribute(String name) {
		try {
			return RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * session 정보로 부터 attribute 설정 method
	 * 
	 * @param name
	 * @param object
	 */
	public static void setAttribute(String name, Object object) {
		RequestContextHolder.getRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * session 정보에 설정한 attribute 삭제
	 * 
	 * @param name
	 */
	public static void removeAttribute(String name) {
		RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
	}

	/**
	 * session id 리턴 
	 *
	 * @param void
	 * @return String SessionId 값
	 */
	public static String getSessionId() {
		return RequestContextHolder.getRequestAttributes().getSessionId();
	}
}