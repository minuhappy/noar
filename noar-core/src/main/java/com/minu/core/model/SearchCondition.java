package com.minu.core.model;

/**
 * 조회 조건
 * 
 * @author Minu.Kim
 */
public class SearchCondition {

	/**
	 * 쿼리 필드 명
	 */
	private String name;

	/**
	 * 쿼리 Operator
	 */
	private String operator;
	
	/**
	 * 조건 값
	 */
	private Object value;

	public SearchCondition() {
	}

	public SearchCondition(String name, String operator, Object value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}