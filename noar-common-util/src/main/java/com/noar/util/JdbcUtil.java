package com.minu.base.util;



import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.minu.base.ConfigConstants;

public class JdbcUtil {
	private static JdbcTemplate jdbcTemplate;
	private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public static JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null) {
			String name = PropertyUtil.getProperty(ConfigConstants.JDBC_TEMPLATE, "jdbcTemplate");
			jdbcTemplate = BeanUtil.get(name, JdbcTemplate.class);
		}
		return jdbcTemplate;
	}
	
	public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		if (namedParameterJdbcTemplate == null) {
			String name = PropertyUtil.getProperty(ConfigConstants.JDBC_TEMPLATE_NAMEDPARAMETER, "namedParameterJdbcTemplate");
			namedParameterJdbcTemplate = BeanUtil.get(name, NamedParameterJdbcTemplate.class);
		}
		return namedParameterJdbcTemplate;
	}
}
