package com.noar.core.system.base;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ServiceInfo {
	private String name;
	private String description;
	private String requestUri;
	private String httpMethod;
	private String requestBody;
	private PathVariables pathVariable;
	private Map<String, String> parameterMap;

	private Object bean;
	private Method method;

	private Class<?> inputType;
	private Class<?> outputType;
	private List<DataType> types;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public Map<String, String> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public PathVariables getPathVariable() {
		return pathVariable;
	}

	public void setPathVariable(PathVariables pathVariable) {
		this.pathVariable = pathVariable;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Class<?> getInputType() {
		return inputType;
	}

	public void setInputType(Class<?> inputType) {
		this.inputType = inputType;
	}

	public Class<?> getOutputType() {
		return outputType;
	}

	public void setOutputType(Class<?> outputType) {
		this.outputType = outputType;
	}

	public List<DataType> getTypes() {
		return types;
	}

	public void setTypes(List<DataType> types) {
		this.types = types;
	}

	public class DataType {
		private String name;
		private Map<String, ?> constants;
		private List<DataField> fields;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Map<String, ?> getConstants() {
			return constants;
		}

		public void setConstants(Map<String, ?> constants) {
			this.constants = constants;
		}

		public List<DataField> getFields() {
			return fields;
		}

		public void setFields(List<DataField> fields) {
			this.fields = fields;
		}
	}

	public class DataField {
		private String name;
		private String type;
		private Object value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}

	public class PathVariables {
		String contextPath;
		String name;
		String param;
		String detail;
		String detailParam;

		public String getContextPath() {
			return contextPath;
		}

		public void setContextPath(String contextPath) {
			this.contextPath = contextPath;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}

		public String getDetail() {
			return detail;
		}

		public void setDetail(String detail) {
			this.detail = detail;
		}

		public String getDetailParam() {
			return detailParam;
		}

		public void setDetailParam(String detailParam) {
			this.detailParam = detailParam;
		}
	}
}