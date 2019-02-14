package com.noar.core.system.handler;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.noar.common.util.JsonUtil;
import com.noar.common.util.PropertyUtil;
import com.noar.common.util.ValueUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.Constants;
import com.noar.core.exception.ServiceException;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.util.OrmUtil;

/**
 * @author Administrator
 */
@Service
public class RestEntityServiceHandler {
	boolean isUnderScoreCase;

	public RestEntityServiceHandler() {
		isUnderScoreCase = ValueUtil.toBoolean(PropertyUtil.getProperty(ConfigConstants.ENTITY_SERVICE_UNDERSOCRE_JSON), true);
	}

	/**
	 * Entity 기반의 Rest Service를 수행
	 * 
	 * @param serviceInfo
	 * @param httpMethod
	 * @param inputParam
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(ServiceInfo serviceInfo) throws Throwable {
		Class<?> clazz = serviceInfo.getBean().getClass();

		String httpMethod = serviceInfo.getHttpMethod();
		String param = serviceInfo.getPathVariable().getParam();

		Map<String, String> paramMap = serviceInfo.getParameterMap();

		// Get
		if (ValueUtil.isEqual(Constants.HTTP_GET, httpMethod))
			return this.doGet(clazz, param, paramMap);

		// Delete
		if (ValueUtil.isEqual(Constants.HTTP_DELETE, httpMethod))
			return this.doDelete(clazz, param, paramMap);

		// Validation check
		String requestBody = serviceInfo.getRequestBody();
		if (ValueUtil.isEmpty(requestBody))
			throw new ServiceException("Reqeust Body is empty.");

		Object input = JsonUtil.jsonToObjectOrList(requestBody, clazz, isUnderScoreCase);

		// Insert
		if (ValueUtil.isEqual(Constants.HTTP_POST, httpMethod))
			return this.doPost(clazz, input);

		// Update
		if (ValueUtil.isEqual(Constants.HTTP_PUT, httpMethod))
			return this.doPut(clazz, input);

		throw new ServiceException("Method Type(" + httpMethod + ") is not supported.");
	}

	@SuppressWarnings("rawtypes")
	private Object doGet(Class<?> clazz, String urlParam, Map parameterMap) throws Exception {
		if (ValueUtil.isNotEmpty(urlParam)) {
			return OrmUtil.select(clazz, urlParam); // Select One
		} else {
			return OrmUtil.selectList(clazz, parameterMap); // List
		}
	}

	@SuppressWarnings("unchecked")
	private <T> Object doPost(Class<T> clazz, Object input) throws Exception {
		if (input instanceof List) {
			OrmUtil.insertBatch((List<T>) input);
		} else {
			OrmUtil.insert(clazz, input);
		}

		return input;
	}

	@SuppressWarnings("unchecked")
	private <T> Object doPut(Class<T> clazz, Object input) throws Exception {
		if (input instanceof List) {
			OrmUtil.updateBatch((List<T>) input);
		} else {
			OrmUtil.update(clazz, input);
		}

		return input;
	}

	@SuppressWarnings("unchecked")
	private <T> boolean doDelete(Class<T> clazz, String serviceName, Object input) throws Exception {
		if (ValueUtil.isNotEmpty(serviceName)) {
			OrmUtil.delete(clazz, serviceName);
		} else {
			if (input instanceof List) {
				OrmUtil.deleteBatch((List<T>) input);
			} else {
				OrmUtil.delete(clazz, input);
			}
		}
		return true;
	}
}