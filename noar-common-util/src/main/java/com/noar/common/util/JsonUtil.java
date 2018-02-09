package com.noar.common.util;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	/**
	 * Convert Object To Pretty JSON String
	 * 
	 * @param item
	 * @return
	 */
	public static String toJsonString(Object item) {
		return toJsonString(item, true);
	}

	/**
	 * Convert Object To JSON String
	 * 
	 * @param item
	 * @param pretty
	 * @return
	 */
	public static String toJsonString(Object item, boolean pretty) {
		if (pretty) {
			return new GsonBuilder().setPrettyPrinting().create().toJson(item);
		} else {
			return new Gson().toJson(item);
		}
	}

	/**
	 * Convert JSON String To Object
	 * 
	 * @param jsonStr
	 * @param inputType
	 * @return
	 */
	public static <T> T jsonToObject(String jsonStr, Class<T> inputType) {
		Gson gson = new Gson();
		return gson.fromJson(jsonStr, inputType);
	}

	/**
	 * Convert Object To JSON String
	 * 
	 * @param item
	 * @return
	 */
	public static String toUnderScoreJsonString(Object item) {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		return gson.toJson(item);
	}

	/**
	 * Convert JSON String To Object
	 * 
	 * @param jsonStr
	 * @param inputType
	 * @return
	 */
	public static <T> T underScoreJsonToObject(String jsonStr, Class<T> inputType) {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		return gson.fromJson(jsonStr, inputType);
	}

	/**
	 * json Content를 JSONArray로 파싱
	 * 
	 * @param content
	 * @return
	 */
	public static JSONArray parseJsonArray(String content) {
		JSONParser jsonParser = new JSONParser();
		try {
			return (JSONArray) jsonParser.parse(content);
		} catch (org.json.simple.parser.ParseException e) {
			throw new IllegalArgumentException("Failed to parse : " + e.getMessage(), e);
		}
	}
}