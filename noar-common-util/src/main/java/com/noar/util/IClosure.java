package com.minu.base.util;

/**
 * <pre>
 * com.minu.base.util
 * IClosure.java
 * 
 * Ŭ���� ���� : Closure(�ڵ� ���) �������̽� �Դϴ�.
 * �ٸ� Util �� ���� doScope(���� ����) �ÿ� ����մϴ�.
 * doScope Method �� Closure ���� ������ ���Ŀ� ���������� ����Ǵ� ��/���� �۾��� ó���� �� �ֽ��ϴ�.
 * </pre>
 */
public interface IClosure {
	public Object execute() throws Throwable;
}
