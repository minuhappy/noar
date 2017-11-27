package com.minu.core.system.controller.Advice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.minu.core.exception.SystemException;
import com.minu.core.util.ValueUtil;

/**
 * RestController에 대한 Controller Advice (ExceptionHandler)
 * 
 * @author Minu
 */
@ControllerAdvice(annotations = RestController.class)
public class ExceptionAdvice {

	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

	@ExceptionHandler(value = { Throwable.class })
	public ResponseEntity<Object> handleGeneralException(HttpServletRequest req, HttpServletResponse res, Throwable ex) {
		logger.error("Error", ex);
		Exception e = ValueUtil.unwrapException(ex);
		e.printStackTrace();
		return null;
	}

	@ExceptionHandler(value = { SystemException.class })
	public Object handleElidomException(HttpServletRequest req, HttpServletResponse res, SystemException ex) {
		logger.error("Error", ex);
		return null;
	}
}