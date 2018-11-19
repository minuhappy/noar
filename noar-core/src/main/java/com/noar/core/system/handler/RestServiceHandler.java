package com.noar.core.system.handler;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.noar.common.util.IScope;
import com.noar.common.util.JsonUtil;
import com.noar.common.util.PropertyUtil;
import com.noar.common.util.SynchCtrlUtil;
import com.noar.common.util.ThreadUtil;
import com.noar.common.util.ValueUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.Constants;
import com.noar.core.exception.ServerException;
import com.noar.core.exception.ServiceException;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.util.OrmUtil;
import com.noar.dbist.annotation.Table;
import com.noar.dbist.dml.Query;

/**
 * @author Administrator
 */
@Service
public class RestServiceHandler {
	boolean isUnderScoreCase;

	public RestServiceHandler() {
		isUnderScoreCase = ValueUtil.toBoolean(PropertyUtil.getProperty(ConfigConstants.ENTITY_SERVICE_UNDERSOCRE_JSON), true);
	}
	
	@EventListener({ ContextRefreshedEvent.class })
	public void init() throws Exception {
		// Table Name & Entity Mapping
		ThreadUtil.doAsynch(() -> this.doTableEntityMapping());
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
	public Object invoke(HttpServletRequest req) throws Throwable {
		String httpMethod = req.getMethod();

		ServiceInfo serviceInfo = parseServiceInfo(req);
		String urlParam = ValueUtil.isNotEmpty(serviceInfo.getUrlParam()) ? String.valueOf(serviceInfo.getUrlParam()) : null;

		Class<?> clazz = serviceInfo.getBean().getClass();

		// 조회
		if (ValueUtil.isEqual(Constants.HTTP_GET, httpMethod)) {
			return this.doGet(clazz, urlParam, req.getParameterMap());
		}

		// Validation check
		String requestBody = this.getInputJsonParam(req);
		if (ValueUtil.isEmpty(requestBody)) {
			throw new ServiceException("Reqeust Body is empty.");
		}

		Object input;
		boolean isArray = JsonUtil.isJsonArray(requestBody);

		if (!isArray) {
			input = JsonUtil.jsonToObject(requestBody, clazz, isUnderScoreCase);
		} else {
			input = JsonUtil.jsonArrayToObjectList(requestBody, clazz, isUnderScoreCase);
		}

		switch (httpMethod) {
			case Constants.HTTP_POST :
				return this.doPost(clazz, urlParam, requestBody, input);
			case Constants.HTTP_PUT :
				return this.doPut(clazz, input);
			case Constants.HTTP_DELETE :
				return this.doDelete(clazz, urlParam, input);
		}

		throw new ServiceException("Method Type(" + httpMethod + ") is not supported.");
	}

	/**
	 * Request를 통해 Service 객체 추출.
	 * 
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	public ServiceInfo parseServiceInfo(final HttpServletRequest req) throws Throwable {
		return parseServiceInfo(req.getRequestURI());
	}

	public ServiceInfo parseServiceInfo(String uri) throws Throwable {
		final String reqPath = uri.endsWith("/") ? uri.substring(0, uri.lastIndexOf('/')) : uri;

		// URL을 변환한 Service Path를 통해, 호출할 Service 경로 추출.
		return SynchCtrlUtil.wrap(reqPath, new HashMap<String, ServiceInfo>(), reqPath, new IScope<ServiceInfo>() {
			@Override
			public ServiceInfo execute() throws Exception {
				String[] pathArr = StringUtils.tokenizeToStringArray(uri, "/");
				String tableName = StringUtils.replace(pathArr[1], "-", "_");

				// Get Service Class
				Class<?> entityClass = getEntityByTableName(tableName);
				if (entityClass == null)
					throw new ServiceException("Invalide Entity Name : " + tableName);

				ServiceInfo service = new ServiceInfo();
				service.setBean(entityClass.newInstance());

				if (pathArr.length > 2) {
					service.setUrlParam(pathArr[2]);
				}

				return service;
			}
		});
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
	private <T> Object doPost(Class<T> clazz, String urlParam, String requestBody, Object input) throws Exception {
		// Page 조회.
		if (ValueUtil.isEqual(urlParam, Constants.ENTITY_SEARCH)) {
			Query query = JsonUtil.jsonToObject(requestBody, Query.class, isUnderScoreCase);
			return OrmUtil.selectPage(clazz, query);
		}

		// Delete
		if (ValueUtil.isEqual(urlParam, Constants.ENTITY_DELETE)) {
			return doDelete(clazz, null, input);
		}

		if (input instanceof List) {
			List<T> list = (List<T>) input;
			OrmUtil.insertBatch(list);
		} else {
			OrmUtil.insert(clazz, input);
		}
		return input;
	}

	@SuppressWarnings("unchecked")
	private <T> Object doPut(Class<T> clazz, Object input) throws Exception {
		if (input instanceof List) {
			List<T> list = (List<T>) input;
			OrmUtil.updateBatch(list);
		} else {
			OrmUtil.update(clazz, input);
		}
		return input;
	}

	@SuppressWarnings("unchecked")
	private <T> boolean doDelete(Class<T> clazz, String urlParam, Object input) throws Exception {
		if (ValueUtil.isNotEmpty(urlParam)) {
			OrmUtil.delete(clazz, urlParam);
		} else {
			if (input instanceof List) {
				List<T> list = (List<T>) input;
				OrmUtil.deleteBatch(list);
			} else {
				OrmUtil.delete(clazz, input);
			}
		}
		return true;
	}

	/**
	 * Request에서 전송된 Json Prameter를 추출.
	 * 
	 * @param request
	 * @return
	 */
	private String getInputJsonParam(HttpServletRequest request) {
		StringBuffer sb = null;

		try {
			sb = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			throw new ServerException("Failed to read request body!", e);
		}

		return sb.toString();
	}

	private Map<String, Class<?>> ENTITY_MAP = new ConcurrentHashMap<String, Class<?>>();

	/**
	 * Table 이름과 Entity 객체 맵핑.
	 * 
	 * @throws Exception
	 */
	private void doTableEntityMapping() throws Exception {
		if (!ENTITY_MAP.isEmpty())
			return;

		String basePackage = PropertyUtil.getProperty(ConfigConstants.BASE_ENTITY_PATH, "com.noar");
		String[] entityPaths = StringUtils.tokenizeToStringArray(basePackage, ",");

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Table.class));

		for (String entityPath : entityPaths) {
			for (BeanDefinition bd : scanner.findCandidateComponents(entityPath)) {
				Class<?> clazz = Class.forName(bd.getBeanClassName());
				ENTITY_MAP.put(clazz.getAnnotation(Table.class).name(), clazz);
			}
		}
	}

	/**
	 * 테이블 명을 이용하여, Entity Class가져오기 실행.
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private Class<?> getEntityByTableName(String tableName) throws Exception {
		this.doTableEntityMapping();
		return ENTITY_MAP.get(tableName);
	}
}