package com.noar.core.system.handler;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.noar.common.util.IScope;
import com.noar.common.util.JsonUtil;
import com.noar.common.util.PropertyUtil;
import com.noar.common.util.SynchCtrlUtil;
import com.noar.common.util.ValueUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.Constants;
import com.noar.core.exception.ServerException;
import com.noar.core.exception.ServiceException;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.util.CrudUtil;
import com.noar.dbist.annotation.Table;
import com.noar.dbist.dml.Query;

/**
 * @author Administrator
 */
public class EntityServiceHandler {

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
					throw new ServiceException("Invalide Table Name : " + tableName);

				ServiceInfo service = new ServiceInfo();
				service.setBean(entityClass.newInstance());

				if (pathArr.length > 2) {
					service.setUrlParam(pathArr[2]);
				}

				return service;
			}
		});
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
	public Object invoke(HttpServletRequest req, ServiceInfo serviceInfo) throws Throwable {
		String httpMethod = req.getMethod();
		String urlParam = ValueUtil.isNotEmpty(serviceInfo.getUrlParam()) ? String.valueOf(serviceInfo.getUrlParam()) : null;

		Class<?> clazz = serviceInfo.getBean().getClass();

		if (ValueUtil.isEqual(RequestMethod.GET.name(), httpMethod)) {
			if (ValueUtil.isNotEmpty(urlParam)) {
				return CrudUtil.select(clazz, urlParam); // Select One
			} else {
				return CrudUtil.selectList(clazz, req.getParameterMap()); // List
			}
		}

		String requestBody = this.getInputJsonParam(req);
		Object input = JsonUtil.underScoreJsonToObject(requestBody, clazz);

		switch (httpMethod) {
		case Constants.HTTP_POST :
			if (ValueUtil.isEqual(urlParam, Constants.ENTITY_SEARCH)) {
				Query query = JsonUtil.underScoreJsonToObject(requestBody, Query.class);
				return CrudUtil.selectPage(clazz, query);
			} else {
				CrudUtil.insert(clazz, input);
			}
			break;
		case Constants.HTTP_PUT :
			CrudUtil.update(clazz, input);
			break;
		case Constants.HTTP_DELETE :
			if (ValueUtil.isNotEmpty(urlParam)) {
				CrudUtil.delete(clazz, urlParam);
			} else {
				List<?> list = CrudUtil.selectList(clazz, input);
				CrudUtil.deleteBatch(list);
			}
			return true;
		}

		return input;
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
	 * 테이블 명을 이용하여, Entity Class가져오기 실행.
	 * 
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	private Class<?> getEntityByTableName(String tableName) throws Exception {
		if (ENTITY_MAP.isEmpty()) {
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

		return ENTITY_MAP.get(tableName);
	}
}