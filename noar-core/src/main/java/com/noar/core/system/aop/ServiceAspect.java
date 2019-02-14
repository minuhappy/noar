package com.noar.core.system.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.noar.core.exception.ServerException;
import com.noar.core.util.TransactionUtil;

/**
 * 클라이언트 서비스 요청을 받아 처리하는 서비스 핸들러에 대한 Filtering Aspect
 * 
 * @author Minu
 */
@Aspect
@Component
public class ServiceAspect {

	// @Pointcut("execution(* com.noar.core..*Biz.*(..)) || execution(* com.noar.core.system.handler.RestServiceHandler.invoke(..))")
	// public void targetMethod() {
	// }

	/**
	 * 서비스 핸들러가 실행되기 전 Filtering 과정을 수행한다.
	 * 
	 * @param joinPoint
	 */
	// @Around("targetMethod()")
	// @Around("execution(* com.noar.core..*Biz.*(..)) || execution(* com.noar.core.system.handler.RestServiceHandler.invoke(..))")
	@Around("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.web.bind.annotation.RequestMapping)")
	public Object invoke(ProceedingJoinPoint call) throws Throwable {
		/**
		 * Meta-Info
		 */
		final Class<? extends Object> clazz = call.getTarget().getClass();
		final Method method = this.getMethod(call.getSignature());
		String name = new StringBuffer().append(clazz.getName()).append(".").append(method.getName()).toString();

		return TransactionUtil.doScope(name, null, () -> call.proceed());
	}

	/**
	 * Get Method
	 * 
	 * @param signature
	 * @return
	 */
	private Method getMethod(Signature signature) {
		try {
			Method getMethod = signature.getClass().getMethod("getMethod");
			if (getMethod == null)
				return null;

			if (!getMethod.isAccessible())
				getMethod.setAccessible(true);

			return (Method) getMethod.invoke(signature);
		} catch (Exception e) {
			throw new ServerException("Check signature concrete type: " + signature.getClass());
		}
	}
}