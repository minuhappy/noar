package com.noar.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Minu.Kim
 */
public class ValueUtil {
	private static final Logger logger = LoggerFactory.getLogger(ValueUtil.class);

	public static final String DATEPATTERN_XSDDATETIME = "yyyy-MM-dd'T'HH:mm:ss'.'S";
	public static final String DATEPATTERN_XSDSMALLDATETIME = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String DATEPATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss'.'S";
	public static final String DATEPATTERN_SMALLDATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEPATTERN_DATE = "yyyy-MM-dd";
	public static final String DATEPATTERN_DATETIME_SHORT = "yyyyMMddHHmmssS";
	public static final String DATEPATTERN_SMALLDATETIME_SHORT = "yyyyMMddHHmmss";
	public static final String DATEPATTERN_DATE_SHORT = "yyyyMMdd";

	public static final int DELIMITERCASETYPE_LOWER = 0;
	public static final int DELIMITERCASETYPE_UPPER = 1;
	public static final int DELIMITERCASETYPE_UPPERANDLOWER = 2;

	private static final List<String> DATEPATTERN_LIST;
	static {
		DATEPATTERN_LIST = new ArrayList<String>();
		for (Field field : ValueUtil.class.getFields()) {
			int mod = field.getModifiers();
			if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
				String name = field.getName();
				if (name.startsWith("DATEPATTERN_") && String.class.equals(field.getType())) {
					try {
						DATEPATTERN_LIST.add((String) field.get(null));
					} catch (IllegalArgumentException e) {
						logger.warn(e.getMessage(), e);
					} catch (IllegalAccessException e) {
						logger.warn(e.getMessage(), e);
					}
				}
			}
		}
	}

	/**
	 * Check Empty
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isEmpty(Object data) {
		if (data == null) {
			return true;
		} else if (data instanceof String || data instanceof StringBuffer) {
			String str = data.toString().trim();
			return str.isEmpty() || str.equalsIgnoreCase("null");
		} else if (data instanceof Object[]) {
			return ((Object[]) data).length == 0;
		} else if (data instanceof Collection<?>) {
			return ((Collection<?>) data).isEmpty();
		} else if (data instanceof Map<?, ?>) {
			return ((Map<?, ?>) data).isEmpty();
		}
		return false;
	}

	/**
	 * Check not Empty
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isNotEmpty(Object data) {
		return !isEmpty(data);
	}

	/**
	 * a Param과 b Param이 동일한지 확인.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEqual(Object a, Object b) {
		if (a == null && b == null) {
			return true;
		} else if (a == null || b == null) {
			return false;
		} else if (a.equals(b)) {
			return true;
		} else if (a instanceof List && b instanceof List) {
			List<Object> aList = (List<Object>) a;
			List<Object> bList = (List<Object>) b;
			if (aList.isEmpty() && bList.isEmpty()) {
				return true;
			} else if (aList.size() != bList.size()) {
				return false;
			}
			int i = 0;
			for (Object aObj : aList) {
				if (isNotEqual(aObj, bList.get(i++))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * a Param과 b Param이 동일하지 않은지 확인.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean isNotEqual(Object a, Object b) {
		return !isEqual(a, b);
	}

	/**
	 * Empty Check
	 * 
	 * @param name
	 * @param value
	 */
	public static void assertNotEmpty(String name, Object value) {
		if (isEmpty(value))
			throw new IllegalArgumentException(name + " is empty.");
	}

	/**
	 * 지정한 Line 수 많큼의 Error Trace를 String 형태로 변환.(개발시 화면에 표시하기 위함.)
	 * 
	 * @param e
	 * @return
	 */
	public static String getErrorStackTraceToString(Throwable e) {
		if (ValueUtil.isEmpty(e)) {
			return null;
		}

		// TODO 환경 설정 적용
		int traceSize = 10;
		StackTraceElement[] traces = e.getStackTrace();
		int size = traces.length;
		if (size > 10) {
			size = traceSize;
		}

		StringBuffer errTrace = new StringBuffer();
		errTrace.append(e.toString()).append("\n");
		for (int i = 0; i < size; i++) {
			errTrace.append("\t").append(traces[i].toString()).append("\n");
		}

		return errTrace.toString();
	}

	/**
	 * Error Trace를 String 형태로 변환.(로그 파일 생성 시 사용)
	 * 
	 * @param e
	 * @return
	 */
	public static String getAllErrorStackTraceToString(Throwable e) {
		if (ValueUtil.isEmpty(e)) {
			return null;
		}
		// create new StringWriter object
		StringWriter sWriter = new StringWriter();

		// create PrintWriter for StringWriter
		PrintWriter pWriter = new PrintWriter(sWriter);

		// now print the stacktrace to PrintWriter we just created
		e.printStackTrace(pWriter);

		// use toString method to get stacktrace to String from StringWriter
		// object
		String strStackTrace = sWriter.toString();

		return strStackTrace;
	}

	/**
	 * Check value.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T checkValue(Object data, T defaultValue) {
		return !isEmpty(data) ? (T) data : defaultValue;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> toSet(T... value) {
		if (isEmpty(value))
			return new HashSet<T>(0);
		Set<T> set = new HashSet<T>(value.length);
		for (T v : value)
			set.add(v);
		return set;
	}

	public static Map<String, String> toMap(String... keyValues) {
		Map<String, String> map = new HashMap<String, String>();
		if (isEmpty(keyValues))
			return map;
		for (String keyValue : keyValues) {
			if (isEmpty(keyValue))
				continue;
			if (keyValue.contains(":")) {
				int index = keyValue.indexOf(":");
				String key = keyValue.substring(0, index).trim();
				String value = keyValue.substring(index + 1).trim();
				map.put(key, value);
			} else {
				map.put(keyValue, null);
			}
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toRequiredType(Object value, Class<T> requiredType) {
		assertNotEmpty("requiredType", requiredType);
		if (value == null)
			return null;
		if (requiredType.isAssignableFrom(value.getClass()))
			return (T) value;
		if (requiredType == String.class)
			return (T) toString(value);
		if (requiredType.equals(Character.class) || requiredType.equals(char.class))
			return (T) toCharacter(value);
		if (requiredType.equals(Date.class))
			return (T) toDate(value);
		if (requiredType.equals(Boolean.class) || requiredType.equals(boolean.class))
			return (T) toBoolean(value);
		if (requiredType.equals(BigDecimal.class))
			return (T) toBigDecimal(value);
		if (requiredType.equals(Integer.class) || requiredType.equals(int.class))
			return (T) toInteger(value);
		if (requiredType.equals(Long.class) || requiredType.equals(long.class))
			return (T) toLong(value);
		if (requiredType.equals(Double.class) || requiredType.equals(double.class))
			return (T) toDouble(value);
		if (requiredType.equals(Short.class) || requiredType.equals(short.class))
			return (T) toShort(value);
		if (requiredType.equals(Float.class) || requiredType.equals(float.class))
			return (T) toFloat(value);
		if (requiredType.equals(Byte.class) || requiredType.equals(byte.class))
			return (T) toByte(value);
		if (requiredType.equals(Byte[].class) || requiredType.equals(byte[].class))
			return (T) toBytes(value);
		throw new IllegalArgumentException("Unsupported requiredType: " + requiredType);
	}

	public static String toString(Object value) {
		return toString(value, null);
	}

	public static String toString(Object value, String defaultValue) {
		if (value == null)
			return defaultValue;
		if (!isEmpty(value))
			return toStringFromObject(value);
		if (defaultValue == null)
			return value == null ? null : value.toString();
		return isEmpty(defaultValue) ? toStringFromObject(value) : defaultValue;
	}

	private static String toStringFromObject(Object value) {
		if (value instanceof Date)
			return toDateString((Date) value, DATEPATTERN_XSDDATETIME);
		if (value instanceof Double)
			return BigDecimal.valueOf((Double) value).toString();
		// if (value instanceof Float)
		// return new BigDecimal((Float) value).toString();
		return value.toString();
	}

	public static Boolean toBoolean(Object value) {
		return toBoolean(value, null);
	}

	private final static Set<String> TRUE_STRING_SET = toSet("true", "TRUE", "t", "T", "yes", "YES", "Yes", "y", "Y", "on", "1");

	public static Boolean toBoolean(Object value, Boolean defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Boolean)
			return (Boolean) value;
		String str = value.toString().trim();
		if (NumberUtils.isNumber(str))
			return NumberUtils.toInt(str) > 0;
		return TRUE_STRING_SET.contains(str);
	}

	public static String toDateString(Date value, String pattern) {
		assertNotEmpty("pattern", pattern);
		return value == null ? null : new SimpleDateFormat(pattern).format(value);
	}

	public static Date toDate(Object value) {
		return toDate(value, (Date) null);
	}

	public static Date toDate(Object value, Date defaultValue) {
		if (isEmpty(value))
			return defaultValue;
		if (value instanceof Date)
			return (Date) value;
		for (String pattern : DATEPATTERN_LIST) {
			try {
				return new SimpleDateFormat(pattern).parse(value.toString());
			} catch (Exception e) {
			}
		}
		throw new IllegalArgumentException("Couldn't find date pattern for value: " + value);
	}

	public static Date toDate(String value, String pattern) {
		if (isEmpty(value))
			return null;
		try {
			return new SimpleDateFormat(pattern).parse(value);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Couldn't parse value: " + value + " for data pattern: " + pattern, e);
		}
	}

	public static Character toCharacter(Object value) {
		return toCharacter(value, null);
	}

	public static Character toCharacter(Object value, Character defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Character)
			return (Character) value;
		String str = toString(value);
		if (str.length() == 0)
			return defaultValue;
		return str.charAt(0);
	}

	public static Short toShort(Object value) {
		return toShort(value, null);
	}

	public static Short toShort(Object value, Short defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Short)
			return (Short) value;
		String str = value.toString();
		if (NumberUtils.isNumber(str)) {
			int index = str.indexOf('.');
			if (index != -1)
				str = str.substring(0, index);
			return NumberUtils.toShort(str);
		}
		return defaultValue;
	}

	public static Integer toInteger(Object value) {
		return toInteger(value, null);
	}

	public static Integer toInteger(Object value, Integer defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Integer)
			return (Integer) value;
		String str = value.toString();
		if (NumberUtils.isNumber(str)) {
			int index = str.indexOf('.');
			if (index != -1)
				str = str.substring(0, index);
			return NumberUtils.toInt(str);
		}
		return defaultValue;
	}

	public static Long toLong(Object value) {
		return toLong(value, null);
	}

	public static Long toLong(Object value, Long defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Long)
			return (Long) value;
		if (value instanceof Date)
			return ((Date) value).getTime();
		String str = value.toString();
		if (NumberUtils.isNumber(str)) {
			int index = str.indexOf('.');
			if (index != -1)
				str = str.substring(0, index);
			return NumberUtils.toLong(str);
		}
		return defaultValue;
	}

	public static Float toFloat(Object value) {
		return toFloat(value, null);
	}

	public static Float toFloat(Object value, Float defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Float)
			return (Float) value;
		String str = value.toString();
		if (NumberUtils.isNumber(str))
			return NumberUtils.toFloat(str);
		return defaultValue;
	}

	public static Double toDouble(Object value) {
		return toDouble(value, null);
	}

	public static Double toDouble(Object value, Double defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Double)
			return (Double) value;
		String str = value.toString();
		if (NumberUtils.isNumber(str))
			return NumberUtils.toDouble(str);
		return defaultValue;
	}

	public static BigDecimal toBigDecimal(Object value) {
		return toBigDecimal(value, null);
	}

	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof BigDecimal)
			return (BigDecimal) value;
		String str = value.toString();
		return NumberUtils.isNumber(str) ? new BigDecimal(str) : defaultValue;
	}

	public static Byte toByte(Object value) {
		return toByte(value, null);
	}

	public static Byte toByte(Object value, Byte defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof Byte)
			return (Byte) value;
		String str = value.toString();
		return NumberUtils.toByte(str, defaultValue);
	}

	public static byte[] toBytes(Object value) {
		return toBytes(value, null);
	}

	public static byte[] toBytes(Object value, byte[] defaultValue) {
		if (value == null)
			return defaultValue;
		if (value instanceof byte[])
			return (byte[]) value;
		String str = value.toString();
		return str.getBytes();
	}

	/**
	 * Convert delimiterCaseValue(such as underscores or hyphens) to camelCaseValue
	 * 
	 * <pre>
	 * <b>Underscores:</b> (all lower, all caps or both character cases)
	 * to_camel_case123  -> toCamelCase123
	 * to_camel_case_123 -> toCamelCase123
	 * TO_CAMEL_CASE123  -> toCamelCase123
	 * TO_CAMEL_CASE_123 -> toCamelCase123
	 * To_Camel_Case123  -> toCamelCase123
	 * To_Camel_Case_123 -> toCamelCase123
	 * 
	 * <b>Hyphens:</b>
	 * to-camel-case123  -> toCamelCase123
	 * to-camel-case-123 -> toCamelCase123
	 * TO-CAMEL-CASE123  -> toCamelCase123
	 * TO-CAMEL-CASE-123 -> toCamelCase123
	 * To-Camel-Case123  -> toCamelCase123
	 * To-Camel-Case-123 -> toCamelCase123
	 * </pre>
	 * 
	 * @param delimiterCaseValue
	 * @param delimiter
	 * @return camel case value
	 */
	public static String toCamelCase(String delimiterCaseValue, Character delimiter) {
		return toCamelCase(delimiterCaseValue, delimiter, false);
	}

	public static String toCamelCase(String delimiterCaseValue, Character delimiter, boolean upper) {
		if (isEmpty(delimiterCaseValue) || delimiter == null)
			return delimiterCaseValue;
		StringBuffer buf = new StringBuffer();
		boolean first = true;
		boolean wasDelimiter = false;
		for (char c : delimiterCaseValue.toCharArray()) {
			// Delimiter
			if (c == delimiter) {
				if (first || wasDelimiter)
					buf.append(c);
				else
					wasDelimiter = true;
			}
			// UpperCase
			else if (c > 64 && c < 91) {
				if (wasDelimiter) {
					buf.append(c);
					wasDelimiter = false;
				} else if (first) {
					buf.append(upper ? c : (char) (c + 32));
					first = false;
				} else {
					buf.append((char) (c + 32));
				}
			}
			// LowerCase
			else if (c > 96 && c < 123) {
				if (wasDelimiter) {
					buf.append((char) (c - 32));
					wasDelimiter = false;
				} else if (first) {
					buf.append(upper ? (char) (c - 32) : c);
					first = false;
				} else {
					buf.append(c);
				}
			}
			// The others
			else {
				if (wasDelimiter)
					wasDelimiter = false;
				buf.append(c);
			}
		}
		return buf.toString();
	}

	/**
	 * InvocationTargetException 의 경우, TargetException 으로 벗겨 냄.
	 * 
	 * @param t
	 * @return
	 */
	public static Exception unwrapException(Exception e) {
		return unwrapException((Throwable) e);
	}

	public static Exception unwrapException(Throwable t) {
		Throwable e = t;
		int i = 0;
		while (e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
			if (i++ > 10)
				break;
		}
		return e instanceof Exception ? (Exception) e : new Exception(e);
	}
	
	/**
	 * Proxy 객체를 원래의 타입으로 변환.
	 * 
	 * @param obj
	 * @return
	 */
//	public static <T> T deProxy(T obj) {
//		if (obj == null)
//			return obj;
//		if (obj instanceof HibernateProxy) {
//			HibernateProxy proxy = (HibernateProxy) obj;
//			LazyInitializer li = proxy.getHibernateLazyInitializer();
//			return (T) li.getImplementation();
//		}
//		return obj;
//	}
	
	/**
	 * 화면에 에러 메시지를 표시하기 위한 형태로 객체 변경.
	 * 
	 * @param e
	 * @return
	 * @throws Exception
	 */
//	public static Object toMessage(Throwable t) throws Exception {
//		AbstractEntity abstractInOutData = new AbstractEntity();
//		abstractInOutData.setSuccess(false);
//		Exception e = unwrapException(t);
//
//		// 정보성 Error의 경우 Message의 내용만 Setting하여 Return
//		if (e instanceof ServiceException) {
//			abstractInOutData.setMessage(e.getMessage());
//			return abstractInOutData;
//		}
//
//		// TODO Exception 타입별 Message 지정 해야 함.
//		String message = null;
//		String detailMessage = null;
//		if (e instanceof SystemException) {
//			// AbstractException 구현체는 UserMessage를 우선 적용.
//			String userMsg = ((SystemException) e).getMessage();
//			message = ValueUtils.isEmpty(userMsg) ? e.getMessage() : userMsg;
//		}
//		// Normal Bug Exception
//		else if (e instanceof NullPointerException) {
//			message = "NullPointerException";
//		}
//		// DB Program Bug Exception
//		else if (e instanceof BadSqlGrammarException) {
//			message = "BadSqlGrammarException";
//		} else if (e instanceof DataIntegrityViolationException) {
//			message = "DataIntegrityViolationException";
//		}
//		// DB Server Exception
//		else if (t instanceof SQLRecoverableException) {
//			message = "SQLRecoverableException";
//		} else if (t instanceof TransientDataAccessResourceException) {
//			message = "TransientDataAccessResourceException";
//		} else if (t instanceof SQLTimeoutException) {
//			message = "SQLTimeoutException";
//		} else {
//			message = e.getMessage();
//		}
//
//		if (ValueUtil.isEmpty(detailMessage)) {
//			detailMessage = e.toString();
//		}
//
//		abstractInOutData.setMessage(message);
//		abstractInOutData.setDetailMessage(detailMessage);
//		abstractInOutData.setErrTrace(ValueUtil.getErrorStackTraceToString(e));
//
//		LOGGER.error(ValueUtil.getAllErrorStackTraceToString(e));
//
//		return abstractInOutData;
//	}
}