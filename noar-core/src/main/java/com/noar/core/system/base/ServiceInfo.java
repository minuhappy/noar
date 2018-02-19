package com.noar.core.system.base;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceInfo {
	private String name;
	private String description;
	@JsonIgnore
	private Object bean;
	@JsonIgnore
	private Method method;
	@JsonIgnore
	private String inputJsonParam;
	@JsonIgnore
	private Object urlParam;

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

	public String getInputJsonParam() {
		return inputJsonParam;
	}

	public void setInputJsonParam(String inputJsonParam) {
		this.inputJsonParam = inputJsonParam;
	}

	public Object getUrlParam() {
		return urlParam;
	}

	public void setUrlParam(Object urlParam) {
		this.urlParam = urlParam;
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

	public static class DataType {
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

	public static class DataField {
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
}