/**
 * 
 */
package com.minu.core.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.sql.SQLRecoverableException;
import java.sql.SQLTimeoutException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.util.ClassUtils;

import com.google.gson.Gson;
import com.minu.core.entity.AbstractEntity;
import com.minu.core.exception.ServerException;
import com.minu.core.exception.ServiceException;
import com.minu.core.exception.SystemException;
import com.minu.core.system.Constants;

import net.sf.common.util.ValueUtils;

/**
 * @author Minu.Kim
 *
 */
@SuppressWarnings("unchecked")
public class ValueUtil extends ValueUtils {
	private static Logger LOGGER = LoggerFactory.getLogger(ValueUtil.class);

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
			if (i++ > 10) {
				break;
			}
		}
		return e instanceof Exception ? (Exception) e : new Exception(e);
	}

	/**
	 * 화면에 에러 메시지를 표시하기 위한 형태로 객체 변경.
	 * 
	 * @param e
	 * @return
	 * @throws Exception
	 */
	public static Object toMessage(Throwable t) throws Exception {
		AbstractEntity abstractInOutData = new AbstractEntity();
		abstractInOutData.setSuccess(false);
		Exception e = unwrapException(t);

		// 정보성 Error의 경우 Message의 내용만 Setting하여 Return
		if (e instanceof ServiceException) {
			abstractInOutData.setMessage(e.getMessage());
			return abstractInOutData;
		}

		// TODO Exception 타입별 Message 지정 해야 함.
		String message = null;
		String detailMessage = null;
		if (e instanceof SystemException) {
			// AbstractException 구현체는 UserMessage를 우선 적용.
			String userMsg = ((SystemException) e).getMessage();
			message = ValueUtils.isEmpty(userMsg) ? e.getMessage() : userMsg;
		}
		// Normal Bug Exception
		else if (e instanceof NullPointerException) {
			message = "NullPointerException";
		}
		// DB Program Bug Exception
		else if (e instanceof BadSqlGrammarException) {
			message = "BadSqlGrammarException";
		} else if (e instanceof DataIntegrityViolationException) {
			message = "DataIntegrityViolationException";
		}
		// DB Server Exception
		else if (t instanceof SQLRecoverableException) {
			message = "SQLRecoverableException";
		} else if (t instanceof TransientDataAccessResourceException) {
			message = "TransientDataAccessResourceException";
		} else if (t instanceof SQLTimeoutException) {
			message = "SQLTimeoutException";
		} else {
			message = e.getMessage();
		}

		if (ValueUtil.isEmpty(detailMessage)) {
			detailMessage = e.toString();
		}

		abstractInOutData.setMessage(message);
		abstractInOutData.setDetailMessage(detailMessage);
		abstractInOutData.setErrTrace(ValueUtil.getErrorStackTraceToString(e));

		LOGGER.error(ValueUtil.getAllErrorStackTraceToString(e));

		return abstractInOutData;
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
	public static <T> T getValue(Object data, T defaultValue) {
		return !isEmpty(data) ? (T) data : defaultValue;
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
	 * To boolean case
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static Boolean toBoolean(Object data) {
		return toBoolean(data, null);
	}

	public static Boolean toBoolean(Object data, Boolean defaultValue) {
		if (isEmpty(data)) {
			return defaultValue;
		} else if (data instanceof Boolean) {
			return (Boolean) data;
		}
		String str = data.toString().trim();
		if (str.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	/**
	 * Delimiter까지 잘라내기 실행.
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String subStringPlusOne(String str) {
		return subStringPlusOne(str, ".");
	}

	public static String subStringPlusOne(String str, String delimeter) {
		if (isEmpty(str)) {
			return "";
		}
		return str.substring(str.lastIndexOf(".") + 1);
	}

	/**
	 * a Param과 b Param이 동일한지 확인.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
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
	 * Convert Object To JSON String
	 * 
	 * @param item
	 * @return
	 */
	public static String toJsonString(Object item) {
		Gson gson = new Gson();
		return gson.toJson(item);
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
	 * source의 각 null이 아닌 필드값을 target에 복사한다.
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	@SuppressWarnings("all")
	public static <S, T> T cloneObject(S source, T target, String... excludeFields) {

		Class<?> sourceClass = source.getClass();
		Class<?> targetClass = target.getClass();
		Field[] sourceFields = sourceClass.getDeclaredFields();

		for (int i = 0; i < sourceFields.length; i++) {
			Field sourceField = sourceFields[i];

			// source class의 private이고 static이 아니고 excludeFields에 포함되지 않은 필드에
			// 대해서만 target으로 필드값 복사
			if (sourceField.getModifiers() == Modifier.PRIVATE && sourceField.getModifiers() != Modifier.STATIC && !isInclude(excludeFields, sourceField.getName())) {
				try {
					sourceField.setAccessible(true);
					Object value = sourceField.get(source);

					if (value != null) {
						Field targetField = targetClass.getDeclaredField(sourceField.getName());
						targetField.setAccessible(true);
						targetField.set(target, value);
					}
				} catch (Exception e) {
					throw new ServiceException("Failed to copy entity data!", e);
				}
			}
		}

		return target;
	}

	/**
	 * array안에 val값과 동일한 값이 있는지 체크
	 * 
	 * @param arr
	 * @param val
	 * @return
	 */
	public static boolean isInclude(String[] array, String val) {
		if (array == null || array.length == 0) {
			return false;
		}

		for (int i = 0; i < array.length; i++) {
			if (ValueUtil.isEqual(array[i], val)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Proxy 객체를 원래의 타입으로 변환.
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> T deProxy(T obj) {
		if (obj == null)
			return obj;
		if (obj instanceof HibernateProxy) {
			HibernateProxy proxy = (HibernateProxy) obj;
			LazyInitializer li = proxy.getHibernateLazyInitializer();
			return (T) li.getImplementation();
		}
		return obj;
	}

	/**
	 * 객체 내의 Field 값 가져오기를 실행.
	 * 
	 * @param clazz
	 * @param columnName
	 * @return
	 * @throws Exception
	 */
	public static Object getFieldValue(Object clazz, String columnName) {
		return getFieldValue(clazz, columnName, false);
	}

	public static Object getFieldValue(Object clazz, String columnName, boolean isSuperClass) {
		Class<?> sourceClass = clazz.getClass();
		if (isSuperClass) {
			sourceClass = sourceClass.getSuperclass();
		}

		Field field = FieldUtils.getDeclaredField(sourceClass, (String) columnName, true);

		try {
			return field.get(clazz);
		} catch (Exception e) {
			return null;
		}
	}

	public static Object getSuperClass(Object clazz) {
		try {
			return clazz.getClass().getSuperclass().newInstance();
		} catch (Exception e) {
			return clazz;
		}
	}

	/**
	 * String Append.
	 * 
	 * @param data
	 * @return
	 */
	public static String append(String... data) {
		StringBuffer sb = new StringBuffer();
		for (String value : data) {
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * File 내용 불러오기 실행.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String getValueByFilePath(String path) {
		if (ValueUtil.isEmpty(path)) {
			return null;
		}
		try {
			return IOUtils.toString(new ClassPathResource(path.trim()).getInputStream(), Constants.ENCODE_UTF_8);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), e);
		}
	}
	
	/**
	 * 자신을 호출한 Class의 정보 추출.
	 * 
	 * @param sqlFileName
	 * @return
	 * @throws Exception
	 */
	public static String getPath(String sqlFileName) throws Exception {
		ClassLoader cl = ClassUtils.getDefaultClassLoader();
		URL url = (cl != null) ? cl.getResource(sqlFileName) : ClassLoader.getSystemResource(sqlFileName);
		if (url != null) {
			return sqlFileName;
		}

		StackTraceElement[] trace = new Throwable().getStackTrace();
		String classPath = trace[1].getClassName();
		// AbstractService에서 호출 되었을 경우, 상위 클래스를 재검색하여 Setting.
		// if (classPath.equals("com.minu.core.service.AbstractService")) {
		// classPath = trace[2].getClassName();
		// }

		String packagePath = StringUtils.substring(classPath, 0, classPath.lastIndexOf("."));

		StringBuilder sqlFilePath = new StringBuilder();
		sqlFilePath.append(StringUtils.replace(packagePath, ".", "/"));
		sqlFilePath.append("/").append(sqlFileName);
		String filePath = sqlFilePath.toString();

		if (cl.getResource(filePath) == null) {
			String userMessage = "파일이 존재하지 않습니다.";
			String consolMessage = userMessage + "( " + filePath + ")";

			ServerException ex = new ServerException(consolMessage);
			ex.setUserMessage(userMessage);
			throw ex;
		}

		return filePath;
	}
}