package com.noar.core.system.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.noar.common.util.ClassUtil;
import com.noar.common.util.IScope;
import com.noar.common.util.JsonUtil;
import com.noar.common.util.PropertyUtil;
import com.noar.common.util.SynchCtrlUtil;
import com.noar.common.util.ThreadPropertyUtil;
import com.noar.common.util.ValueUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.Constants;
import com.noar.core.exception.SystemException;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.util.CrudUtil;

/**
 * @author Administrator
 */
public class RestServiceHandler {
	private static final Map<String, ServiceInfo> REST_CACHE = new ConcurrentHashMap<String, ServiceInfo>();

	/**
	 * Request를 통해 Service 객체 추출.
	 * 
	 * @param request
	 * @return
	 * @throws Throwable
	 */
	public ServiceInfo get(final HttpServletRequest req) throws Throwable {
		String name = req.getRequestURI();
		if (name.endsWith("/")) {
			name = name.substring(0, name.lastIndexOf("/"));
		}

		if (REST_CACHE.containsKey(name))
			return REST_CACHE.get(name);
		return get(name);
	}

	public ServiceInfo get(String name) throws Throwable {
		// Set Service Info.
		Map<String, Object> serviceInfoMap = setServiceInfoMap(name);
		// URL을 변환한 Service Path를 통해, 호출할 Service 경로 추출.
		String reqPath = String.valueOf(serviceInfoMap.get(Constants.REQUEST_PATH));
		if (REST_CACHE.containsKey(reqPath))
			return REST_CACHE.get(reqPath);
		return SynchCtrlUtil.wrap(reqPath, REST_CACHE, reqPath, new IScope<ServiceInfo>() {
			@Override
			public ServiceInfo execute() throws Exception {
				Class<?> entityClass = (Class<?>) serviceInfoMap.get(Constants.ENTITY_CLASS);
				if (entityClass == null) {
					throw new SystemException("Entity is not exist.");
				}
				// Make ServiceInfo Object.
				ServiceInfo service = new ServiceInfo();
				service.setBean(entityClass.newInstance());
				return service;
			}
		});
	}

	/**
	 * Entity 기반의 Rest Service를 수행
	 * 
	 * @param serviceInfo
	 * @param method
	 * @param inputParam
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(ServiceInfo serviceInfo, String method, String inputParam) throws Throwable {
		Class<?> entity = serviceInfo.getBean().getClass();

		if (ValueUtil.isEqual(RequestMethod.GET, method) && ValueUtil.isEmpty(inputParam)) {
			// List 구현
			return null;
		}

		// Read
		if (ValueUtil.isEqual(RequestMethod.GET, method)) {
			 return CrudUtil.select(inputParam);
		}

		Object input = JsonUtil.jsonToObject(inputParam, entity);
		if (ValueUtil.isEqual(RequestMethod.POST, method)) {
			 CrudUtil.insert(input);
		} else if (ValueUtil.isEqual(RequestMethod.PUT, method)) {
			 CrudUtil.update(input);
		} else if (ValueUtil.isEqual(RequestMethod.DELETE, method)) {
			 CrudUtil.delete(input);
		}

		return input;
	}

	/**
	 * URI를 통해 Service 정보 가져오기 실행.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private String getServicePath(String uri) {
		int startIndex = 0;
		int lastIndex = uri.lastIndexOf(".");

		if (uri.startsWith("/")) {
			startIndex = uri.indexOf("/") + 1;
		}
		if (lastIndex < 0) {
			lastIndex = uri.length();
		}

		StringBuffer sb = new StringBuffer();
		sb.append(PropertyUtil.getProperty(ConfigConstants.BASE_PATH, "com.noar.core"));
		sb.append(Constants.DOT);
		sb.append((uri.substring(startIndex, lastIndex)).replaceAll("/", "."));

		return sb.toString();
	}

	/**
	 * URI를 통하여 Package path, Class path, Method Name 등의 Service 정보를 추출.
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> setServiceInfoMap(String uri) throws Exception {
		if (uri.endsWith("/")) {
			uri = uri.substring(0, uri.lastIndexOf('/'));
		}

		// Get Service Class
		Class<?> entityClass = null;
		String requestPath = null;
		String param = null;

		List<Map<String, String>> listMap = getClassInfo(uri);
		for (Map<String, String> map : listMap) {
			try {
				String packagePath = map.get(Constants.PACKAGE_PATH);
				String className = map.get(Constants.CLASS_NAME);
				// className = className.substring(0, className.lastIndexOf('s'));
				requestPath = map.get(Constants.REQUEST_PATH);
				param = map.get(Constants.REST_PARAM);

				// 하이픈(-)이 포함되어 있을 경우, 제거 또는 CamelCase로 변경.
				packagePath = StringUtils.replace(packagePath, "-", "_");
				className = ValueUtil.toCamelCase(className, '-', true);

				StringBuilder sb = new StringBuilder();
				sb.append(packagePath).append(Constants.DOT).append(className);

				entityClass = ClassUtil.forName(sb.toString());
			} catch (Exception e) {
				entityClass = null;
				requestPath = null;
				param = null;
				continue;
			}
			break;
		}

		// Set Service Info.
		Map<String, Object> serviceInfoMap = new HashMap<String, Object>();
		serviceInfoMap.put(Constants.REQUEST_PATH, requestPath);
		serviceInfoMap.put(Constants.ENTITY_CLASS, entityClass);
		// param
		ThreadPropertyUtil.put(Constants.REST_PARAM, param);
		return serviceInfoMap;
	}

	/**
	 * URI를 통하여 Class 정보 추출
	 * 
	 * @param uri
	 * @return
	 */
	private List<Map<String, String>> getClassInfo(String uri) {
		List<Map<String, String>> list = new ArrayList<>();
		String servicePath = getServicePath(uri);
		// http://127.0.0.1:8080/service/class/method
		{
			Map<String, String> map = new HashMap<String, String>();
			String classPath = servicePath.substring(0, servicePath.lastIndexOf(Constants.DOT));
			String packagePath = servicePath.substring(0, classPath.lastIndexOf(Constants.DOT));
			String className = classPath.substring(0, classPath.lastIndexOf(Constants.DOT) + 1);
			String restParam = servicePath.substring(0, classPath.lastIndexOf(Constants.DOT) + 1);

			map.put(Constants.REQUEST_PATH, uri.substring(0, uri.lastIndexOf("/")));
			map.put(Constants.PACKAGE_PATH, packagePath);
			map.put(Constants.CLASS_PATH, classPath);
			map.put(Constants.CLASS_NAME, className);
			map.put(Constants.REST_PARAM, restParam);
			list.add(map);
		}
		// http://127.0.0.1:8080/service/class
		{
			// Rest-List
			Map<String, String> map = new HashMap<String, String>();
			String packagePath = servicePath.substring(0, servicePath.lastIndexOf(Constants.DOT));
			String className = servicePath.substring(0, servicePath.lastIndexOf(Constants.DOT) + 1);

			map.put(Constants.REQUEST_PATH, uri);
			map.put(Constants.CLASS_PATH, servicePath);
			map.put(Constants.PACKAGE_PATH, packagePath);
			map.put(Constants.CLASS_NAME, className);

			list.add(map);
		}
		return list;
	}
}