//package com.noar.common.util;
//
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.anyframe.online.runtime.service.request.RequestContextUtil;
//import com.samsung.gmes2.base.ConfigConstants;
//import com.samsung.gmes2.base.Constants;
//
//public class MethodUtil {
//	private static final Logger LOGGER = LoggerFactory.getLogger(MethodUtil.class);
//
//	/**
//	 * <pre>
//	 * 처리내용 : Method 영역을 수행합니다.
//	 * </pre>
//	 * 
//	 * @Method Name : doScope
//	 * @param name
//	 * @param closure
//	 * @return
//	 * @throws Throwable
//	 */
//	public static Object doScope(String name, IClosure closure) throws Throwable {
//		String className = null;
//		String methodName = null;
//		if (ValueUtil.isNotEmpty(name)) {
//			if (name.contains(".")) {
//				int index = name.lastIndexOf(".");
//				className = name.substring(0, index);
//				methodName = name.substring(index + 1);
//			} else {
//				methodName = name;
//			}
//		}
//		return doScope(className, methodName, closure);
//	}
//
//	/**
//	 * <pre>
//	 * 처리내용 : Method 영역을 수행합니다.
//	 * </pre>
//	 * 
//	 * @Method Name : doScope
//	 * @param className
//	 * @param methodName
//	 * @param closure
//	 * @return
//	 * @throws Throwable
//	 */
//	public static Object doScope(final String className, final String methodName, final IClosure closure) throws Throwable {
//		return ThreadPropertyUtil.doScope(new IClosure() {
//			@Override
//			public Object execute() throws Throwable {
//				// Method Stack 시작지점이면,
//				if (isNewScope() && ValueUtil.isNotEmpty(methodName)) {
//					// ReadOnly 영역인지 여부 설정 (ReadOnly 인 경우 Thread Cache 를 이용하지
//					// 않음)
//					for (String prefix : PREFIX_READONLY_LIST) {
//						if (methodName.startsWith(prefix)) {
//							setReadOnlyScope(true);
//							break;
//						}
//					}
//
//					String dataAccsTypeCode = null;
//
//					// Target DataSource 설정 (RO/RW) / DW
//					// BI olap DataSource
//					if (DataUtil.getFctCode().equals(Constants.FCTCODE_HQ)) {
//						try {
//							List<String> methodList = null;
//							ExceptionUtil exceptionUtil = BeanUtil.get("biOlapExceptionUtil", ExceptionUtil.class);
//							if (ValueUtil.isNotEmpty(exceptionUtil)) {
//								methodList = exceptionUtil.getMethodList(className);
//							}
//							if (ValueUtil.isNotEmpty(methodList)) {
//								if (methodList.contains(methodName)) {
//									dataAccsTypeCode = Constants.DATAACCSTYPECODE_BIOLAP;
//								}
//							}
//						} catch (Exception ex) {
//							LOGGER.error(
//									"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!No bean named 'biOlapExceptionUtil' is defined!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
//									ex.getMessage());
//						}
//					}
//
//					if (ValueUtil.isEmpty(dataAccsTypeCode)) {
//						// 검사DB(Exadata) DataSource
//						try {
//							List<String> qaMethodList = null;
//							ExceptionUtil qaExceptionUtil = BeanUtil.get("qaExceptionUtil", ExceptionUtil.class);
//							if (ValueUtil.isNotEmpty(qaExceptionUtil)) {
//								qaMethodList = qaExceptionUtil.getMethodList(className);
//							}
//							if (ValueUtil.isNotEmpty(qaMethodList)) {
//								if (qaMethodList.contains(methodName)) {
//									dataAccsTypeCode = Constants.DATAACCSTYPECODE_QA;
//								}
//							}
//						} catch (Exception ex) {
//							LOGGER.error(
//									"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!No bean named 'qaExceptionUtil' is defined!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",
//									ex.getMessage());
//						}
//					}
//
//					// 로깅은 RW
//					if (ValueUtil.isEmpty(dataAccsTypeCode) && ValueUtil.isNotEmpty(className) && className.endsWith("ScreenRTLoggingApp")) {
//						dataAccsTypeCode = Constants.DATAACCSTYPECODE_RW;
//					}
//
//					// jsp는 RO
//					if (SecurityUtil.isJspScreen()) {
//						dataAccsTypeCode = Constants.DATAACCSTYPECODE_RO;
//					}
//
//					// String dataAccsTypeCode = null;
//					// final String sysScreenId =
//					// ReflectionUtil.toSysScreenId(className);
//
//					// DW
//					// if(DataUtil.getFctCode().equals(Constants.FCTCODE_HQ)){
//					// if(ValueUtil.isNotEmpty(methodList)){
//					// if(methodList.contains(methodName)){
//					// dataAccsTypeCode = Constants.DATAACCSTYPECODE_BIOLAP;
//					// }
//					// }
//					// }
//
//					final String sysScreenId = ReflectionUtil.toSysScreenId(className);
//					// DataSource 분기
//					// 메뉴테이블에서 조회
//					if (ValueUtil.isEmpty(dataAccsTypeCode)) {
//						dataAccsTypeCode = SynchCtrlUtil.doScope(CacheUtil.CACHENAME_DATAACCSTYPECODE, sysScreenId, String.class, new IClosure() {
//							@Override
//							public Object execute() throws Throwable {
//								if (ValueUtil.isEmpty(sysScreenId)) {
//									return Constants.DATAACCSTYPECODE_RW;
//								}
//								String dataType = (String) TransactionUtil.doScope("getTbmSmMenuMagtDataAccsType", new IClosure() {
//									@Override
//									public Object execute() throws Throwable {
//										String dataType = Constants.DATAACCSTYPECODE_RW;
//										Map<String, String> menuMap = new HashMap<String, String>();
//										menuMap.put("fctCode", DataUtil.getFctCode());
//										menuMap.put("screenSn", sysScreenId);
//										List<Map<String, Object>> menuMagtList = JdbcUtil.getNamedParameterJdbcTemplate().queryForList(dataAccsTypeCodeSql,
//												menuMap);
//
//										if (ValueUtil.isNotEmpty(menuMagtList)) {
//											dataType = (String) menuMagtList.get(0).get("DATA_ACCS_TYPE_CODE");
//										}
//										return dataType;
//									}
//								});
//								if (dataType == null || Constants.DATAACCSTYPECODE_RW.equals(dataType)) {
//									return Constants.DATAACCSTYPECODE_RW;
//								} else {
//									return dataType;
//								}
//							}
//						});
//					}
//					RequestContextUtil.put(Constants.CTXKEY_DATAACCSTYPECODE, dataAccsTypeCode);
//
//					if (ValueUtil.isNotEmpty(dataAccsTypeCode) && Constants.DATAACCSTYPECODE_RO.equals(dataAccsTypeCode)) {
//						RequestContextUtil.put(Constants.CTXKEY_DATASOURCENAME, DataUtil.getProperty(ConfigConstants.DATASOURCE_READONLY, "businessDataSource"));
//					} else if (ValueUtil.isNotEmpty(dataAccsTypeCode) && Constants.DATAACCSTYPECODE_BIOLAP.equals(dataAccsTypeCode)) {
//						RequestContextUtil.put(Constants.CTXKEY_DATASOURCENAME, DataUtil.getProperty(ConfigConstants.DATASOURCE_BIOLAP, "businessDataSource"));
//					} else if (ValueUtil.isNotEmpty(dataAccsTypeCode) && Constants.DATAACCSTYPECODE_QA.equals(dataAccsTypeCode)) {
//						RequestContextUtil.put(Constants.CTXKEY_DATASOURCENAME, DataUtil.getProperty(ConfigConstants.DATASOURCE_QA, "businessDataSource"));
//					} else {
//						RequestContextUtil.put(Constants.CTXKEY_DATASOURCENAME, DataUtil.getProperty(ConfigConstants.DATASOURCE, "businessDataSource"));
//					}
//				}
//
//				// Method Stack 추가
//				StringBuffer buf = new StringBuffer();
//				if (ValueUtil.isNotEmpty(className)) {
//					String prefix;
//					if (className.contains(".")) {
//						prefix = className.substring(className.lastIndexOf(".") + 1);
//					} else {
//						prefix = className;
//					}
//					buf.append(prefix).append(".");
//				}
//				buf.append(methodName);
//				put(buf.toString());
//
//				try {
//					return closure.execute();
//				} finally {
//					// Method Stack 해제
//					pop();
//					// Method Stack 의 끝지점이면,
//					if (isNewScope()) {
//						// 해제 예약된 Cache 해제
//						CacheUtil.invalidateBatch();
//					}
//				}
//			}
//		});
//	}
//
//	private static final String PROP_STACK = "MethodUtil.stack";
//	private static final String PROP_READONLYSCOPE = "MethodUtil.readOnlyScope";
//
//	public static boolean isScope() {
//		return ThreadPropertyUtil.contains(PROP_STACK);
//	}
//
//	private static void setReadOnlyScope(boolean readOnly) {
//		ThreadPropertyUtil.put(PROP_READONLYSCOPE, readOnly);
//	}
//
//	public static boolean isReadOnlyScope() {
//		return ValueUtil.toBoolean(ThreadPropertyUtil.get(PROP_READONLYSCOPE), false);
//	}
//
//	private static boolean isNewScope() {
//		return ValueUtil.isEmpty(ThreadPropertyUtil.get(PROP_STACK));
//	}
//
//	/**
//	 * <pre>
//	 * 처리내용 : App, Biz, EM, QM 등의 호출된 전체 메써드 경로를 조회합니다.
//	 * <i><클래스명></i>.<i><메써드명></i>/...
//	 * </pre>
//	 * 
//	 * @Method Name : getServicePath
//	 * @return
//	 */
//	public static String getPath() {
//		if (!isScope()) {
//			return null;
//		}
//		@SuppressWarnings("unchecked")
//		List<String> stack = (List<String>) ThreadPropertyUtil.get(PROP_STACK);
//		if (stack.isEmpty()) {
//			return null;
//		}
//
//		StringBuffer buf = new StringBuffer();
//		int i = 0;
//		for (String name : stack) {
//			buf.append(i++ == 0 ? "" : "/").append(name);
//		}
//		return buf.toString();
//	}
//
//	@SuppressWarnings("unchecked")
//	private static void put(String name) {
//		List<String> stack = null;
//		if (isScope()) {
//			stack = (List<String>) ThreadPropertyUtil.get(PROP_STACK);
//		} else {
//			stack = new ArrayList<String>();
//			ThreadPropertyUtil.put(PROP_STACK, stack);
//		}
//		stack.add(name);
//	}
//
//	private static void pop() {
//		if (!isScope()) {
//			return;
//		}
//		@SuppressWarnings("unchecked")
//		List<String> stack = (List<String>) ThreadPropertyUtil.get(PROP_STACK);
//		if (!stack.isEmpty()) {
//			stack.remove(stack.size() - 1);
//		}
//	}
//}