package com.noar.core.system.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.noar.common.util.PropertyUtil;
import com.noar.common.util.SynchCtrlUtil;
import com.noar.common.util.ThreadUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.system.base.ServiceInfo.PathVariables;
import com.noar.dbist.annotation.Table;

@Service
public class RestServiceConverter {
	/**
	 * Request를 통해 Service 객체 추출.
	 * 
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	// public ServiceInfo parseServiceInfo(HttpServletRequest req) throws Throwable {
	// return parseServiceInfo(req.getRequestURI());
	// }

	@SuppressWarnings("unchecked")
	public ServiceInfo parseServiceInfo(HttpServletRequest request) throws Throwable {
		String uri = request.getRequestURI();
		String reqPath = uri.endsWith("/") ? uri.substring(0, uri.lastIndexOf('/')) : uri;

		// URL을 변환한 Service Path를 통해, 호출할 Service 경로 추출.
		return SynchCtrlUtil.doScope(reqPath, new HashMap<String, ServiceInfo>(), reqPath, () -> {
			PathVariables pathVariables = this.getPathVariables(uri);
			String tableName = StringUtils.replace(pathVariables.getName(), "-", "_");

			ServiceInfo service = new ServiceInfo();
			service.setName(tableName);
			service.setRequestUri(uri);
			service.setPathVariable(pathVariables);
			service.setHttpMethod(request.getMethod());
			service.setParameterMap(request.getParameterMap());
			service.setRequestBody(this.getRequestBody(request));

			// Get Service Class
			Class<?> entityClass = getEntityByTableName(tableName);
			if (entityClass != null) service.setBean(entityClass.newInstance());

			return service;
		});
	}

	/**
	 * URI의 Path Variable을 PathVariables 객체로 변환.
	 * 
	 * @param uri
	 * @return
	 */
	private PathVariables getPathVariables(String uri) {
		String[] pathVariableArr = new String[5];
		String[] parseUriVars = StringUtils.tokenizeToStringArray(uri, "/");

		System.arraycopy(parseUriVars, 0, pathVariableArr, 0, parseUriVars.length);

		PathVariables pathVariables = new ServiceInfo().new PathVariables();
		pathVariables.setContextPath(pathVariableArr[0]);
		pathVariables.setName(pathVariableArr[1]);
		pathVariables.setParam(pathVariableArr[2]);
		pathVariables.setDetail(pathVariableArr[3]);
		pathVariables.setDetailParam(pathVariableArr[4]);

		return pathVariables;
	}

	/**
	 * Request에서 전송된 Body 를 추출.
	 * 
	 * @param request
	 * @return
	 */
	private String getRequestBody(HttpServletRequest request) {
		try {
			ServletInputStream inputStream = request.getInputStream();
			if (inputStream == null || inputStream.available() > 0) return null;

			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
				StringBuilder stringBuilder = new StringBuilder();

				char[] charBuffer = new char[256];
				int bytesRead = -1;

				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}

				String value = stringBuilder.toString();
				if (value.isEmpty()) { return null; }

				return stringBuilder.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
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
		this.doTableEntityMapping();
		return ENTITY_MAP.get(tableName);
	}

	/**
	 * Table 이름과 Entity 객체 맵핑.
	 * 
	 * @throws Exception
	 */
	private void doTableEntityMapping() throws Exception {
		if (!ENTITY_MAP.isEmpty()) return;

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

	@EventListener({ ContextRefreshedEvent.class })
	public void init() throws Exception {
		// Table Name & Entity Mapping
		ThreadUtil.doAsynch(() -> this.doTableEntityMapping());
	}
}