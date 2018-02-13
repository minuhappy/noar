package com.noar.common.util;

public interface IScope<T> {
	public T execute() throws Throwable;
}
