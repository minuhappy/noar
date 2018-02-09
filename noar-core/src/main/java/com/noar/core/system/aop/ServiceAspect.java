package com.noar.core.system.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.noar.core.exception.ServerException;
import com.noar.core.util.TransactionUtil;

import net.sf.common.util.Closure;

/**
 * 클라이언트 서비스 요청을 받아 처리하는 서비스 핸들러에 대한 Filtering Aspect
 * 
 * @author Minu
 */
@Aspect
@Component
public class ServiceAspect {
	
	@Pointcut("execution(* com.minu.core..*Biz.*(..)) || execution(* com.minu.core.system.handler.RestServiceHandler.invoke(..))")
	public void targetMethod() {
	}

	/**
	 * 서비스 핸들러가 실행되기 전 Filtering 과정을 수행한다.
	 * 
	 * @param joinPoint
	 */
	@Around("targetMethod()")
	public Object invoke(ProceedingJoinPoint call) throws Throwable {
		/**
		 * Meta-Info
		 */
		final Class<? extends Object> clazz = call.getTarget().getClass();
		final Method method = this.getMethod(call.getSignature());

		// @Transaction 정보 추출
		Transactional transactional = method == null ? null : method.getAnnotation(Transactional.class);

		String name = new StringBuffer().append(clazz.getName()).append(".").append(method.getName()).toString();

		return TransactionUtil.doScope(name, transactional, new Closure<Object, Throwable>() {
			@Override
			public Object execute() throws Throwable {
				return call.proceed();
			}
		});
	}

	/**
	 * Get Method
	 * @param signature
	 * @return
	 */
	private Method getMethod(Signature signature) {
		try {
			Method getMethod = signature.getClass().getMethod("getMethod");
			if (getMethod == null) {
				return null;
			}
			if (!getMethod.isAccessible()) {
				getMethod.setAccessible(true);
			}
			return (Method) getMethod.invoke(signature);
		} catch (Exception e) {
			throw new ServerException("Check signature concrete type: " + signature.getClass());
		}
	}
}