package com.noar.util;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.SQLRecoverableException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.number.NumberFormatter;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.util.StringUtils;

import com.minu.exception.SystemErrorException;
import com.minu.exception.SystemException;
import com.minu.exception.SystemServerException;

public class CastingUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ValueUtil.class);

	public static final String DATEFORMAT_DATETIMEDETAIL = "yyyyMMddHHmmssSSS";
	public static final String DATEFORMAT_DATETIMESHORT = "yyyyMMddHHmmss";
	public static final String DATEFORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATEFORMAT_DATESHORT = "yyyyMMdd";
	public static final String DATEFORMAT_DATE = "yyyy-MM-dd";
	public static final String DATEFORMAT_MONTHSHORT = "yyyyMM";
	public static final String DATEFORMAT_MONTH = "yyyy-MM";

	public static final String TIMEFORMAT_TIME = "HH:mm:ss";
	public static final String TIMEFORMAT_TIMESHORT = "HHmmss";

	public static final int ROUND_UP = BigDecimal.ROUND_UP;
	public static final int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;
	public static final int ROUND_DOWN = BigDecimal.ROUND_DOWN;

	public static final int UNIT_SECOND = Calendar.SECOND;
	public static final int UNIT_MINUTE = Calendar.MINUTE;

	public static final char PADDIRECTION_LEFT = 'L';
	public static final char PADDIRECTION_RIGHT = 'R';

	public static final List<DateFormatter> DATEFORMATTER_LIST;
	public static final List<NumberFormatter> NUMBERFORMATTER_LIST;
	static {
		DATEFORMATTER_LIST = new ArrayList<DateFormatter>();
		NUMBERFORMATTER_LIST = new ArrayList<NumberFormatter>();
		for (Field field : ValueUtil.class.getFields()) {
			if (!Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			String name = field.getName();
			if (name.startsWith("DATEFORMAT_") && field.getType() == String.class) {
				try {
					DATEFORMATTER_LIST.add(new DateFormatter((String) field.get(null)));
				} catch (IllegalArgumentException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					LOGGER.error(e.getMessage(), e);
				}
			} else if (name.startsWith("NUMBERFORMAT_") && field.getType() == String.class) {
				try {
					NUMBERFORMATTER_LIST.add(new NumberFormatter((String) field.get(null)));
				} catch (IllegalArgumentException e) {
					LOGGER.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * <b>data �� String �������� ��ȯ�մϴ�.</b>
	 * 
	 * @param data
	 * @return
	 */
	public static String toString(Object data) {
		return toString(data, null);
	}

	/**
	 * <b>data �� String �������� ��ȯ�մϴ�.</b><br/>
	 * data �� �� ���̸� �⺻ ���� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static String toString(Object data, String defaultValue) {
		if (data == null) {
			return defaultValue;
		} else if (data instanceof String) {
			return (String) data;
		} else if (data instanceof BigDecimal) {
			BigDecimal bd = (BigDecimal) data;
			return bd.toPlainString();
		} else if (data instanceof Timestamp) {
			return toTimestampString(data);
		} else if (data instanceof Date) {
			return toDateString(data);
		} else if (data instanceof byte[]) {
			return new String((byte[]) data);
		}
		return ValueUtil.isEmpty(data) && defaultValue != null ? defaultValue : data.toString();
	}

	/**
	 * data �� String ������ ��ȯ�մϴ�.<br/>
	 * data �� null �� ��� null ���� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static String toNull(Object data) {
		return toString(data, null);
	}

	/**
	 * data �� null �� �ƴ� String ������ ��ȯ�մϴ�.<br/>
	 * data �� null �� ��� �� �� ("") �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static String toNotNull(Object data) {
		return toString(data, "");
	}

	/**
	 * data �� Integer ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Integer toInteger(Object data) {
		return toInteger(data, null);
	}

	/**
	 * �����͸� Integer ������ ��ȯ�մϴ�.<br/>
	 * data �� null�� ��� defaultValue �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static Integer toInteger(Object data, Integer defaultValue) {
		if (ValueUtil.isEmpty(data)) {
			return defaultValue;
		}
		if (data instanceof Integer) {
			return (Integer) data;
		}
		return toNumber(data).intValue();
	}

	/**
	 * data �� Integer ������ ��ȯ�մϴ�.<br/>
	 * data �� null�� ��� 0 �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Integer toIntegerNotNull(Object data) {
		return toInteger(data, 0);
	}

	/**
	 * �����͸� BigDecimal ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object data) {
		return toBigDecimal(data, null);
	}

	/**
	 * data �� BigDecimal ������ ��ȯ�մϴ�.<br/>
	 * data �� null�� ��� defaultValue �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object data, BigDecimal defaultValue) {
		if (ValueUtil.isEmpty(data)) {
			return defaultValue;
		}
		if (data instanceof BigDecimal) {
			return (BigDecimal) data;
		}
		return new BigDecimal(toNumber(data).toString());
	}

	/**
	 * data �� BigDecimal ������ ��ȯ�մϴ�.<br/>
	 * data �� null�� ��� 0 �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static BigDecimal toBigDecimalNotNull(Object data) {
		return toBigDecimal(data, new BigDecimal(0));
	}

	/**
	 * data �� Long ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Long toLong(Object data) {
		return toLong(data, null);
	}

	/**
	 * data �� Long ������ ��ȯ�մϴ�.<br/>
	 * data �� �� ���̸� �⺻ ���� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static Long toLong(Object data, Long defaultValue) {
		if (ValueUtil.isEmpty(data)) {
			return defaultValue;
		}
		if (data instanceof Long) {
			return (Long) data;
		}
		return toNumber(data).longValue();
	}

	/**
	 * data �� Long ������ ��ȯ�մϴ�.<br/>
	 * data �� null�� ��� 0 �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Long toLongNotNull(Object data) {
		return toLong(data, new Long(0));
	}

	/**
	 * data �� Float ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Float toFloat(Object data) {
		return toFloat(data, null);
	}

	/**
	 * data �� Float ������ ��ȯ�մϴ�.<br/>
	 * data �� �� ���̸� �⺻ ���� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static Float toFloat(Object data, Float defaultValue) {
		if (ValueUtil.isEmpty(data)) {
			return defaultValue;
		}
		if (data instanceof Float) {
			return (Float) data;
		}
		return toNumber(data).floatValue();
	}

	/**
	 * data �� Float ������ ��ȯ�մϴ�.<br/>
	 * data �� null �� ��� �� �� ("") �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Float toFloatNotNull(Object data) {
		return toFloat(data, new Float(0));
	}

	/**
	 * data �� Double ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Double toDouble(Object data) {
		return toDouble(data, null);
	}

	/**
	 * data �� Double ������ ��ȯ�մϴ�.<br/>
	 * data �� �� ���̸� �⺻ ���� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static Double toDouble(Object data, Double defaultValue) {
		if (ValueUtil.isEmpty(data)) {
			return defaultValue;
		}
		if (data instanceof Double) {
			return (Double) data;
		}
		return toNumber(data).doubleValue();
	}

	/**
	 * data �� Double ������ ��ȯ�մϴ�.<br/>
	 * data �� null �� ��� �� �� ("") �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Double toDoubleNotNull(Object data) {
		return toDouble(data, new Double(0));
	}

	/**
	 * data �� Number ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	private static Number toNumber(Object data) {
		// TODO Number toNumber(Object data)
		try {
			return NumberFormat.getInstance().parse(data.toString());
		} catch (ParseException e) {
			throw new SystemErrorException("Unsupported number pattern");
		}
	}

	/**
	 * data �� Boolean ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Boolean toBoolean(Object data) {
		return toBoolean(data, null);
	}

	/**
	 * data �� Boolean ������ ��ȯ�մϴ�. data �� �� ���̸� �⺻ ���� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param defaultValue
	 * @return
	 */
	public static Boolean toBoolean(Object data, Boolean defaultValue) {
		if (ValueUtil.isEmpty(data)) {
			return defaultValue;
		} else if (data instanceof Boolean) {
			return (Boolean) data;
		}
		String str = data.toString();
		if (str.equalsIgnoreCase("T") || str.equalsIgnoreCase("Y") || str.equalsIgnoreCase("true")) {
			return true;
		}
		return false;
	}

	/**
	 * data �� Boolean ������ ��ȯ�մϴ�. data �� �� ���̸� false �� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Boolean toBooleanNotNull(Object data) {
		return toBoolean(data, false);
	}

	/**
	 * data �� Date ������ ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @return
	 */
	public static Date toDate(Object data) {
		if (ValueUtil.isEmpty(data)) {
			return null;
		}
		if (data instanceof Date) {
			return (Date) data;
		}
		if (data instanceof Long) {
			return new Date((Long) data);
		}
		String dataStr = data.toString();
		for (DateFormatter formatter : DATEFORMATTER_LIST) {
			try {
				return formatter.parse(dataStr, Locale.ENGLISH);
			} catch (ParseException e) {
				continue;
			} catch (Exception e) {
				continue;
			}
		}
		throw new SystemErrorException("Unsupported date pattern: " + dataStr);
	}

	/**
	 * data �� pattern �� ���� Date ������ ��ȯ�Ͽ� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static Date toDate(String data, String pattern) throws Exception {
		return new SimpleDateFormat(pattern, Locale.ENGLISH).parse(data);
	}

	private static final String COLOR_DIGITS = "0123456789ABCDEF";

	/**
	 * data �� awt Color ������ ��ȯ�մϴ�.
	 * 
	 * @param webColor
	 * @return
	 * @throws Exception
	 */
	public static Color toColor(String webColor) throws Exception {
		return toColor(webColor, 255);
	}

	/**
	 * data �� awt Color ������ ��ȯ�մϴ�.
	 * 
	 * @param webColor
	 * @param alpha
	 * @return
	 * @throws Exception
	 */
	public static Color toColor(String webColor, int alpha) throws Exception {
		if (ValueUtil.isEmpty(webColor, 0)) {
			return Color.BLACK;
		}
		if (ValueUtil.isNumber(webColor)) {
			return new Color(toInteger(webColor));
		}
		int len = webColor.length();
		int i;
		for (i = 0; i < len;) {
			i++;
			if (COLOR_DIGITS.contains(webColor.charAt(len - i) + "")) {
				continue;
			}
			i--;
			break;
		}
		if (i < 6) {
			return Color.BLACK;
		}
		String _webColor = new StringBuffer("0x").append(NumeralConverter.to16(alpha))
				.append(webColor.substring(len - 6)).toString();
		return new Color(toInteger(_webColor));
	}

	/**
	 * Collection ��ü�� String �迭�� ��ȯ�մϴ�.
	 * 
	 * @param <T>
	 * @param col
	 * @return
	 */
	public static <T> String[] toStringArray(Collection<T> col) {
		if (ValueUtil.isEmpty(col)) {
			return new String[0];
		}
		String[] array = new String[col.size()];
		int i = 0;
		for (T obj : col) {
			array[i++] = toString(obj);
		}
		return array;
	}

	/**
	 * data�� ��¥ �� �ð� ���ڿ��� ��ȯ�մϴ�.<br/>
	 * 
	 * @param pattern
	 * @return
	 */
	public static String toDateString(Object data) {
		return toDateString(data, null);
	}

	/**
	 * data�� pattern ������ ��¥ �� �ð� ���ڿ��� ��ȯ�մϴ�.<br/>
	 * 
	 * @param data
	 * @param pattern
	 * @return
	 */
	public static String toDateString(Object data, String pattern) {
		return new DateFormatter(toDatePattern(pattern)).print(toDate(data), Locale.ENGLISH);
	}

	private static String toDatePattern(String pattern) {
		String _pattern = pattern;
		if (ValueUtil.isEmpty(_pattern)) {
			_pattern = DATEFORMAT_DATETIMESHORT;
		} else if (_pattern.length() < 3) {
			_pattern = new StringBuffer("yyyy").append(_pattern).append("MM").append(_pattern).append("dd HH")
					.append(_pattern).append("mm").append(_pattern).append("ss").toString();
		}
		return _pattern;
	}

	public static long toGmtDateLong(Object data) {
		long dateLong = data instanceof Long ? (Long) data : toDate(data).getTime();
		return dateLong - TimeZone.getDefault().getRawOffset();
	}

	/**
	 * timestamp�� pattern ������ ��¥ �� �ð� ���ڿ��� ��ȯ�մϴ�.<br/>
	 * 
	 * @param data
	 * @param pattern
	 * @return
	 */
	private static String toTimestampString(Object data) {
		return toTimestampString(data, null);
	}

	private static String toTimestampString(Object data, String pattern) {
		return new DateFormatter(toTimestampPattern(pattern)).print(toDate(data), Locale.ENGLISH);
	}

	private static String toTimestampPattern(String pattern) {
		String _pattern = pattern;
		if (ValueUtil.isEmpty(_pattern)) {
			_pattern = DATEFORMAT_DATETIMEDETAIL;
		} else if (_pattern.length() < 3) {
			_pattern = new StringBuffer("yyyy").append(_pattern).append("MM").append(_pattern).append("dd HH")
					.append(_pattern).append("mm").append(_pattern).append("ss").append("SSS").toString();
		}
		return _pattern;
	}

	/**
	 * data�� �ð� ���� unit�� ���� ��¥ �� �ð� ���ڿ��� ��ȯ�մϴ�.<br/>
	 * 
	 * @param data
	 * @param unit
	 * @return
	 * @throws
	 */
	public static String toTimeString(int data, int unit) throws Exception {
		long time = data;
		switch (unit) {
		case UNIT_SECOND:
			time *= 1000;
			break;
		case UNIT_MINUTE:
			time *= 60000;
			break;
		default:
			throw new SystemErrorException("Not supported time unit.");
		}
		return toString(new Date(time), TIMEFORMAT_TIME);
	}

	/**
	 * data�� �ð� ���� unit�� ���� day time ������ ���ڿ��� ��ȯ�մϴ�.<br/>
	 * 
	 * @param data
	 * @param unit
	 * @return
	 * @throws
	 */
	public static String toDayTimeString(int data, int unit) throws Exception {
		String timeStr = toTimeString(data, unit);
		int hour = Integer.parseInt(ValueUtil.split(timeStr, ":")[0]);
		if (hour < 24) {
			return new StringBuffer("0D ").append(timeStr).toString();
		}
		return new StringBuffer().append(hour / 24).append("D ").append(hour % 24).append(":")
				.append(timeStr.substring(timeStr.indexOf(":") + 1)).toString();
	}

	/**
	 * data �� pattern �� �´� String ���� ��ȯ�մϴ�.
	 * 
	 * @param data
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static String toNumberString(Object data, String pattern) throws Exception {
		return new DecimalFormat(pattern).format(toDouble(data));
	}

	/**
	 * <pre>
	 * ó������ : t �� RuntimeException ���� ��ȯ �Ǵ� ��ȯ�մϴ�.
	 * </pre>
	 * 
	 * @Method Name : toRuntimeException
	 * @param t
	 * @return
	 */
	public static RuntimeException toRuntimeException(Throwable t) {
		Throwable e = unwrapException(t);
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return toSystemException(e);
	}

	/**
	 * <pre>
	 * ó������ : t �� Gmes2Exception ���� ��ȯ �Ǵ� ��ȯ�մϴ�.
	 * </pre>
	 * 
	 * @Method Name : toGmes2Exception
	 * @param t
	 * @return
	 */
	public static SystemException toSystemException(Throwable t) {
		Throwable e = unwrapException(t);

		SystemException ge = null;
		if (e instanceof SystemException) {
			ge = (SystemException) e;
		}
		// Authentication Exception
		// else if (e instanceof InsufficientAuthenticationException || e
		// instanceof AccessDeniedException) {
		// ge = new
		// SystemAuthException(Gmes2AuthException.CODE_AUTH_INSUFFICIENT, e);
		// } else if (t instanceof BadCredentialsException) {
		// ge = new
		// SystemAuthException(Gmes2AuthException.CODE_AUTH_SSO_USER_NOTFOUND,
		// t);
		// } else if (t instanceof LockedException) {
		// ge = new
		// SystemAuthException(Gmes2AuthException.CODE_AUTH_USER_LOCKED, t);
		// } else if (t instanceof DisabledException) {
		// ge = new
		// SystemAuthException(Gmes2AuthException.CODE_AUTH_USER_DISABLED, t);
		// }
		// Normal Bug Exception
		else if (t instanceof NullPointerException) {
			ge = new SystemErrorException("Null Pointer", t);
		} else if (t instanceof ClassCastException || t instanceof IndexOutOfBoundsException) {
			ge = new SystemErrorException(t);
		}
		// DB Program Bug Exception
		else if (t instanceof BadSqlGrammarException) {
			ge = new SystemErrorException("Bad SQL Grammar", t);
		} else if (t instanceof DataIntegrityViolationException) {
			ge = new SystemErrorException("Data Integrity Viloation", t);
		} else if (t instanceof InvalidDataAccessApiUsageException || t instanceof UnexpectedRollbackException) {
			ge = new SystemErrorException(t);
		}
		// DB Server Exception
		else if (t instanceof SQLRecoverableException) {
			ge = new SystemServerException("Network is unreachable.", t);
		} else if (t instanceof CannotCreateTransactionException) {
			ge = new SystemServerException("Database is unreachable.", t);
		}
		// Transaction Exception
		else if (t instanceof TransactionTimedOutException) {
			ge = new SystemServerException("Transaction timed out.", t);
		} else if (t instanceof TransientDataAccessResourceException) {
			ge = new SystemServerException("DB Transaction timed out.", t);
			ge.setMsgDisplayLoc("S");
		} else if (t instanceof SQLTimeoutException) {
			ge = new SystemServerException("DB Transaction timed out.", t);
		}
		// Else
		else {
			ge = new SystemServerException(e.getMessage(), e);
		}

		return ge;
	}

	public static String toStackTraceString(Throwable t) {
		StringWriter w = new StringWriter();
		t.printStackTrace(new PrintWriter(w));
		return w.toString();
	}

	public static String capitalize(String name) {
		return StringUtils.capitalize(name);
	}

	public static String toCamelCase(final String name) {
		if (ValueUtil.isEmpty(name)) {
			return name;
		}

		String[] names = StringUtils.tokenizeToStringArray(name.toLowerCase(), "_");
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (String n : names) {
			buf.append(i++ == 0 ? n : capitalize(n));
		}
		return buf.toString();
	}

	/**
	 * 
	 * <pre>
	 * ó������ : CamelCase �� UnderscoreCase ��ȯ�Ѵ�.
	 *            n1ExtdCulmCont - N_1_EXTD_CULM_CONT
	 * </pre>
	 * 
	 * @Method Name : toUnderscoreCase
	 * @param name
	 * @return
	 */
	public static String toUnderscoreCase(final String name) {
		if (ValueUtil.isEmpty(name)) {
			return name;
		}

		StringBuffer buf = new StringBuffer();
		char preC = ' ';
		for (char c : name.toCharArray()) {
			if (c == ' ') {
				continue;
			}
			if (preC != '_' && preC != ' ' && c < 91 && (c > '9' || preC > '9')) {
				buf.append("_");
			}
			buf.append(c);
			preC = c;
		}
		return buf.toString().toUpperCase();
	}

	/**
	 * <pre>
	 * ó������ : e �� InvocationTargetException �̶�� TargetException ���� ���ܳ��ϴ�.
	 * </pre>
	 * 
	 * @Method Name : unwrapException
	 * @param e
	 * @return
	 */
	public static Exception unwrapException(Exception e) {
		return unwrapException((Throwable) e);
	}

	/**
	 * <pre>
	 * ó������ : t �� InvocationTargetException �̶�� TargetException ���� ���ܳ��ϴ�.
	 * </pre>
	 * 
	 * @Method Name : unwrapException
	 * @param t
	 * @return
	 */
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
}
