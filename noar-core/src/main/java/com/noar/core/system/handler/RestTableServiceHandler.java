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
public class RestTableServiceHandler {
	boolean isUnderScoreCase;

	public RestTableServiceHandler() {
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
		String tableName = serviceInfo.getName();
		String httpMethod = serviceInfo.getHttpMethod();
		String param = serviceInfo.getPathVariable().getParam();

		Map<String, String> paramMap = serviceInfo.getParameterMap();

		// Get
		if (ValueUtil.isEqual(Constants.HTTP_GET, httpMethod))
			return this.doGet(tableName, param, paramMap);

		// Delete
		if (ValueUtil.isEqual(Constants.HTTP_DELETE, httpMethod))
			return this.doDelete(tableName, param, paramMap);

		// Validation check
		String requestBody = serviceInfo.getRequestBody();
		if (ValueUtil.isEmpty(requestBody))
			throw new ServiceException("Reqeust Body is empty.");

		Object input = JsonUtil.jsonToObjectOrList(requestBody, Map.class, isUnderScoreCase);

		// Insert
		if (ValueUtil.isEqual(Constants.HTTP_POST, httpMethod))
			return this.doPost(tableName, input);

		// Update
		if (ValueUtil.isEqual(Constants.HTTP_PUT, httpMethod))
			return this.doPut(tableName, input);

		throw new ServiceException("Method Type(" + httpMethod + ") is not supported.");
	}

	@SuppressWarnings("rawtypes")
	private Object doGet(String tableName, String urlParam, Map parameterMap) throws Exception {
		if (ValueUtil.isNotEmpty(urlParam)) {
			return OrmUtil.select(tableName, urlParam, Map.class); // Select One
		} else {
			return OrmUtil.selectList(tableName, parameterMap, Map.class); // List
		}
	}
	

	@SuppressWarnings("unchecked")
	private <T> Object doPost(String tableName, Object input) throws Exception {
		if (input instanceof List) {
			OrmUtil.insertBatch(tableName, (List<T>) input);
		} else {
			OrmUtil.insert(tableName, input);
		}

		return input;
	}

	@SuppressWarnings("unchecked")
	private <T> Object doPut(String tableName, Object input) throws Exception {
		if (input instanceof List) {
			OrmUtil.updateBatch(tableName, (List<T>) input);
		} else {
			OrmUtil.update(tableName, input);
		}

		return input;
	}

	@SuppressWarnings("unchecked")
	private <T> boolean doDelete(String tableName, String urlParam, Object input) throws Exception {
		if (ValueUtil.isNotEmpty(urlParam)) {
			OrmUtil.delete(tableName, urlParam);
		} else {
			if (input instanceof List) {
				OrmUtil.deleteBatch((List<T>) input);
			} else {
				OrmUtil.delete(tableName, input);
			}
		}
		return true;
	}
}