package com.minu.core.model;

/**
 * Sort Condition
 * 
 * @author Minu.Kim
 */
public class SortCondition {
	/**
	 * 소팅 필드 명
	 */
	private String name;
	/**
	 * ASC / DESC 여부
	 */
	private boolean ascending;

	public SortCondition() {
	}

	public SortCondition(String name, boolean ascending) {
		this.name = name;
		this.ascending = ascending;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}
}