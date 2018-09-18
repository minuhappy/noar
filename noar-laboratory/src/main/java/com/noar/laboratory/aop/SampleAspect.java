package com.noar.laboratory.aop;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class SampleAspect {

	private Logger logger = LoggerFactory.getLogger(SampleAspect.class);

	private Map<String, Boolean> STATUS_MAP = new ConcurrentHashMap<String, Boolean>();
	private Map<String, Long> ELAPSE_TIME_MAP = new ConcurrentHashMap<String, Long>();

	@Around("@within(org.springframework.web.bind.annotation.RestController)")
	public Object filterAroundAccess(ProceedingJoinPoint joinPoint) throws Throwable {
		String key = Arrays.toString(joinPoint.getArgs());
		// Cache에서 추출하는 구조로 변경.
		{
			Long elapseTime = ELAPSE_TIME_MAP.get(key);
			if (elapseTime != null && elapseTime < 5)
				return joinPoint.proceed();
		}

		Object result = null;
		Boolean status = STATUS_MAP.get(key);

		if (status == null || status) {
			STATUS_MAP.put(key, false);

			long startTime = System.currentTimeMillis();
			result = joinPoint.proceed();
			long elapseTime = System.currentTimeMillis() - startTime;

			STATUS_MAP.put(key, true);
			ELAPSE_TIME_MAP.put(key, elapseTime / 1000);
		} else {
			this.doWait(key);
			result = joinPoint.proceed();
		}

		return result;
	}

	private void doWait(String key) throws Throwable {
		Boolean status = STATUS_MAP.get(key);
		if (status != null && status == false) {
			Thread.sleep(500);
			this.doWait(key);
		}
	}
}