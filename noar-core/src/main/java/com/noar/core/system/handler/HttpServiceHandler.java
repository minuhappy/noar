package com.noar.core.system.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.noar.common.util.BeanUtil;
import com.noar.common.util.ClassUtil;
import com.noar.common.util.JsonUtil;
import com.noar.common.util.PropertyUtil;
import com.noar.common.util.SynchCtrlUtil;
import com.noar.common.util.ValueUtil;
import com.noar.core.ConfigConstants;
import com.noar.core.Constants;
import com.noar.core.exception.ServerException;
import com.noar.core.system.base.ServiceInfo;
import com.noar.core.util.TransactionUtil;

public class HttpServiceHandler {
	private static final Map<String, ServiceInfo> CACHE = new ConcurrentHashMap<String, ServiceInfo>();

	public ServiceInfo get(String name) throws Throwable {
		if (CACHE.containsKey(name))
			return CACHE.get(name);

		return SynchCtrlUtil.doScope(name, CACHE, name, () -> {
			// Set Service Info.
			Map<String, Object> serviceInfoMap = setServiceInfoMap(name);
			Class<?> serviceClass = (Class<?>) serviceInfoMap.get(Constants.SERVICE_CLASS);
			String classPath = (String) serviceInfoMap.get(Constants.CLASS_PATH);
			String methodName = (String) serviceInfoMap.get(Constants.METHOD_NAME);

			// Method Info.
			Method method = getServiceMethod(serviceClass, methodName);

			if (method == null)
				throw new ServerException("Service [" + classPath + "] has no method [" + methodName + "]", null);

			Class<?> inputClass = method.getParameterCount() == 0 ? null : method.getParameterTypes()[0];
			Class<?> outputClass = method.getReturnType();

			// Make ServiceInfo Object.
			ServiceInfo service = new ServiceInfo();
			service.setBean(BeanUtil.get(serviceClass));
			service.setMethod(method);
			service.setInputType(inputClass);
			service.setOutputType(outputClass);

			return service;
		});
	}

	/**
	 * Method Invoke
	 * 
	 * @param service
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(ServiceInfo service, String inputParam) throws Throwable {
		final Object clazz = service.getBean();
		Class<?> inputType = service.getInputType();
		Object inputObj = null;

		// 1. Convert Input String To JSON Object
		try {
			if (ValueUtil.isNotEmpty(inputType)) {
				if (ValueUtil.isNotEmpty(inputParam)) {
					inputObj = JsonUtil.jsonToObject(inputParam, inputType);
				} else {
					inputObj = inputType == null ? null : inputType.newInstance();
				}
			}
		} catch (Throwable th) {
			throw new ServerException("Convert Input String To JSON Object", th);
		}

		final Method method = service.getMethod();
		final Object inputObject = inputObj;

		// 2. Dynamic Invocation
		Object outputObject = TransactionUtil.doScope("ServiceUtil.Invoke", () -> {
			boolean isEmptyParam = method.getParameterCount() == 0;
			return isEmptyParam ? method.invoke(clazz) : method.invoke(clazz, inputObject);
		});

		// 3. Convert Output Object To JSON String
		try {
			return JsonUtil.toJsonString(outputObject);
		} catch (Throwable th) {
			throw new ServerException("Convert Output Object To JSON String", th);
		}
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
		sb.append(PropertyUtil.getProperty(ConfigConstants.BASE_ENTITY_PATH, "com.noar.core"));
		sb.append(Constants.DOT);
		sb.append((uri.substring(startIndex, lastIndex)).replaceAll("/", "."));

		return sb.toString();
	}

	/**
	 * clazz 내부의 메소드 중 public이고 static이 아니고 파라미터 개수가 0 또는 1인 메소드 중에 이름이
	 * methodName인 메소드를 찾아서 리턴
	 * 
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	private Method getServiceMethod(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			// Check Method.
			if (Modifier.isStatic(m.getModifiers()))
				continue;

			// Check Parameter Count.
			if (m.getParameterCount() > 1)
				continue;

			// Check Method Name.
			if (!m.getName().equalsIgnoreCase(methodName))
				continue;

			return m;
		}
		return null;
	}

	/**
	 * URI를 통하여 Package path, Class path, Method Name 등의 Service 정보를 추출.
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> setServiceInfoMap(String uri) throws Exception {
		if (uri.endsWith("/"))
			uri = uri.substring(0, uri.lastIndexOf('/'));

		// Get Service Class
		String servicePath = getServicePath(uri);
		String classPath = servicePath.substring(0, servicePath.lastIndexOf(Constants.DOT));
		String packagePath = servicePath.substring(0, classPath.lastIndexOf(Constants.DOT));
		String className = classPath.substring(0, classPath.lastIndexOf(Constants.DOT) + 1);
		String methodName = servicePath.substring(0, classPath.lastIndexOf(Constants.DOT) + 1);

		// 하이픈(-)이 포함되어 있을 경우, 제거 또는 CamelCase로 변경.
		packagePath = StringUtils.replace(packagePath, "-", "_");
		className = ValueUtil.toCamelCase(className, '-', true) + Constants.BIZ_SUBFIX;
		methodName = ValueUtil.toCamelCase(methodName, '-', false);

		StringBuilder sb = new StringBuilder();
		sb.append(packagePath).append(Constants.DOT).append(className);

		Class<?> serviceClass = ClassUtil.forName(sb.toString());

		// Set Service Info.
		Map<String, Object> serviceInfoMap = new HashMap<String, Object>();
		serviceInfoMap.put(Constants.SERVICE_CLASS, serviceClass);
		serviceInfoMap.put(Constants.METHOD_NAME, methodName);

		return serviceInfoMap;
	}
}