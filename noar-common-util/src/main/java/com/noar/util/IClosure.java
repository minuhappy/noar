package com.minu.base.util;

/**
 * <pre>
 * com.minu.base.util
 * IClosure.java
 * 
 * 클래스 개요 : Closure(코드 블록) 인터페이스 입니다.
 * 다른 Util 로 부터 doScope(영역 수행) 시에 사용합니다.
 * doScope Method 는 Closure 수행 이전과 이후에 공통적으로 수행되는 선/후행 작업을 처리할 수 있습니다.
 * </pre>
 */
public interface IClosure {
	public Object execute() throws Throwable;
}
