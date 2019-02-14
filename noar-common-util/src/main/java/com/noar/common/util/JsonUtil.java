package com.noar.common.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONTokener;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
	private static Gson simpleGson;
	private static Gson simplePrettyGson;
	private static Gson underScoreGson;
	private static Gson underScorePrettyGson;

	static {
		{
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setDateFormat(DateUtil.DEFAULT_DATE_FORMAT);

			simpleGson = gsonBuilder.create();

			gsonBuilder.setPrettyPrinting();
			simplePrettyGson = gsonBuilder.create();
		}

		{
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setDateFormat(DateUtil.DEFAULT_DATE_FORMAT);
			gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

			underScoreGson = gsonBuilder.create();

			gsonBuilder.setPrettyPrinting();
			underScorePrettyGson = gsonBuilder.create();

		}
	}

	/**
	 * Convert Object To JSON String
	 * 
	 * @param item
	 * @return
	 */
	public static String toJsonString(Object item) {
		return toJsonString(item, false, true);
	}

	public static String toJsonString(Object item, boolean isUnderScore) {
		return isUnderScore ? toUnderScoreJsonString(item) : toJsonString(item);
	}

	public static String toJsonString(Object item, boolean isUnderScore, boolean pretty) {
		return isUnderScore ? toUnderScoreJsonString(item, pretty) : toSimpleJsonString(item, pretty);
	}

	public static String toSimpleJsonString(Object item) {
		return simpleGson.toJson(item);
	}

	public static String toSimpleJsonString(Object item, boolean pretty) {
		return pretty ? simplePrettyGson.toJson(item) : simpleGson.toJson(item);
	}

	public static String toUnderScoreJsonString(Object item) {
		return underScoreGson.toJson(item);
	}

	public static String toUnderScoreJsonString(Object item, boolean pretty) {
		return pretty ? underScorePrettyGson.toJson(item) : underScoreGson.toJson(item);
	}

	/**
	 * Convert JSON String To Object
	 * 
	 * @param jsonStr
	 * @param inputType
	 * @return
	 */
	public static <T> T jsonToObject(String jsonStr, Class<T> inputType) {
		return simpleGson.fromJson(jsonStr, inputType);
	}

	public static <T> T jsonToObject(String jsonStr, Class<T> inputType, boolean isUnderScore) {
		return isUnderScore ? underScoreJsonToObject(jsonStr, inputType) : jsonToObject(jsonStr, inputType);
	}

	public static <T> T underScoreJsonToObject(String jsonStr, Class<T> inputType) {
		return underScoreGson.fromJson(jsonStr, inputType);
	}

	public static <T> List<T> jsonToList(Class<T> clazz, String value) {
		Type listType = new TypeToken<ArrayList<T>>() {
		}.getType();
		return simpleGson.fromJson(value, listType);
	}

	public static <T> List<T> jsonArrayToObjectList(String content, Class<T> clazz) throws Exception {
		return jsonArrayToObjectList(content, clazz, false);
	}

	public static <T> List<T> jsonArrayToObjectList(String content, Class<T> clazz, boolean isUnderScore) throws Exception {
		List<T> list = new ArrayList<T>();

		JSONArray jsonArray = parseJsonArray(content);
		for (int i = 0; i < jsonArray.size(); i++) {
			String jsonString = jsonArray.get(i).toString();
			T t = isUnderScore ? underScoreJsonToObject(jsonString, clazz) : jsonToObject(jsonString, clazz);
			list.add(t);
		}

		return list;
	}

	/**
	 * Json String을 Object로 변환.
	 * 
	 * @param jsonStr
	 * @param inputType
	 * @return
	 */
	public static Object jsonToObjectOrList(String jsonStr, Class<?> inputType, boolean isUnderScore) throws Exception {
		if (isJsonArray(jsonStr))
			return JsonUtil.jsonArrayToObjectList(jsonStr, inputType, isUnderScore);
		else
			return JsonUtil.jsonToObject(jsonStr, inputType, isUnderScore);
	}

	/**
	 * json Content를 JSONArray로 파싱
	 * 
	 * @param content
	 * @return
	 */
	private static JSONArray parseJsonArray(String content) throws Exception {
		return (JSONArray) new JSONParser().parse(content);
	}

	public static <T> List<T> underScoreJsonToList(Class<T> clazz, String value) {
		Type listType = new TypeToken<ArrayList<T>>() {
		}.getType();
		return simpleGson.fromJson(value, listType);
	}

	public static boolean isJsonArray(String value) {
		return new JSONTokener(value).nextValue() instanceof org.json.JSONArray;
	}

	// public static boolean isJsonArray(String value) {
	// return ValueUtil.checkValue(StringUtils.trim(value), "").startsWith("[");
	// }
}