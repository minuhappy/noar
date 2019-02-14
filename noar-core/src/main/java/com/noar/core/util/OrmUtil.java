package com.noar.core.util;

import java.util.List;
import java.util.Map;

import com.noar.common.util.BeanUtil;
import com.noar.common.util.ValueUtil;
import com.noar.core.orm.wrapper.DbistWrapper;
import com.noar.core.orm.wrapper.IOrmWrapper;
import com.noar.dbist.dml.Page;
import com.noar.dbist.dml.Query;

/**
 * Entity에 대한 Create, Update, Delete, Search, Find 등의 공통 함수 유틸리티
 * 
 * @author Minu.Kim
 */
public class OrmUtil {

	private static IOrmWrapper ormWrapper;

	// static {
	// getOrmWrapper() = BeanUtil.get(DbistWrapper.class);
	// }

	private static IOrmWrapper getOrmWrapper() {
		return ValueUtil.checkValue(ormWrapper, BeanUtil.get(DbistWrapper.class));
	}

	public static <T> T select(T data) throws Exception {
		return getOrmWrapper().select(data);
	}

	public static <T> T selectWithLock(T data) throws Exception {
		return getOrmWrapper().selectWithLock(data);
	}

	public static <T> T select(Class<T> clazz, Object... pkCondition) throws Exception {
		return getOrmWrapper().select(clazz, pkCondition);
	}

	public static <T> T selectWithLock(Class<T> clazz, Object... pkCondition) throws Exception {
		return getOrmWrapper().select(clazz, pkCondition);
	}

	public static <T> T selectByCondition(T data) throws Exception {
		return getOrmWrapper().selectByCondition(data);
	}

	public static <T> T selectByCondition(Class<T> clazz, T condition) throws Exception {
		return getOrmWrapper().selectByCondition(clazz, condition);
	}

	public static <T> T selectByConditionWithLock(Class<T> clazz, Object condition) throws Exception {
		return getOrmWrapper().selectByConditionWithLock(clazz, condition);
	}

	public static <T> T select(String tableName, Object pkCondition, Class<T> requiredType) throws Exception {
		return getOrmWrapper().select(tableName, pkCondition, requiredType);
	}

	public static <T> T selectWithLock(String tableName, Object pkCondition, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectWithLock(tableName, pkCondition, requiredType);
	}

	public static <T> T selectByCondition(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectByCondition(tableName, condition, requiredType);
	}

	public static <T> T selectByConditionWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectByConditionWithLock(tableName, condition, requiredType);
	}

	public static <T> T selectByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectByQl(ql, paramMap, requiredType);
	}

	public static <T> T selectByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectByQlPath(qlPath, paramMap, requiredType);
	}

	public static <T> T selectBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectBySql(sql, paramMap, requiredType);
	}

	public static <T> T selectBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectBySqlPath(sqlPath, paramMap, requiredType);
	}

	public static void insert(Object data) throws Exception {
		getOrmWrapper().insert(data);
	}

	public static void insertBatch(List<?> list) throws Exception {
		getOrmWrapper().insertBatch(list);
	}

	public static <T> T insert(Class<T> clazz, Object data) throws Exception {
		return getOrmWrapper().insert(clazz, data);
	}

	public static void insertBatch(Class<?> clazz, List<?> list) throws Exception {
		getOrmWrapper().insertBatch(clazz, list);
	}

	public static void insert(String tableName, Object data) throws Exception {
		getOrmWrapper().insert(tableName, data);
	}

	public static void insertBatch(String tableName, List<?> list) throws Exception {
		getOrmWrapper().insertBatch(tableName, list);
	}

	public static void update(Object data) throws Exception {
		getOrmWrapper().update(data);
	}

	public static void updateBatch(List<?> list) throws Exception {
		getOrmWrapper().updateBatch(list);
	}

	public static void update(Object data, String... fieldNames) throws Exception {
		getOrmWrapper().update(data, fieldNames);
	}

	public static void updateBatch(List<?> list, String... fieldNames) throws Exception {
		getOrmWrapper().updateBatch(list, fieldNames);
	}

	public static <T> T update(Class<T> clazz, Object data) throws Exception {
		return getOrmWrapper().update(clazz, data);
	}

	public static <T> T update(Class<T> clazz, Object data, String... fieldNames) throws Exception {
		return getOrmWrapper().update(clazz, data, fieldNames);
	}

	public static void update(String tableName, Object data) throws Exception {
		getOrmWrapper().update(tableName, data);
	}

	public static void updateBatch(String tableName, List<?> list) throws Exception {
		getOrmWrapper().updateBatch(tableName, list);
	}

	public static void update(String tableName, Object data, String... fieldNames) throws Exception {
		getOrmWrapper().update(tableName, data, fieldNames);
	}

	public static void updateBatch(String tableName, List<?> list, String... fieldNames) throws Exception {
		getOrmWrapper().updateBatch(tableName, list, fieldNames);
	}

	public static void upsert(Object data) throws Exception {
		getOrmWrapper().upsert(data);
	}

	public static void upsertBatch(List<?> list) throws Exception {
		getOrmWrapper().upsertBatch(list);
	}

	public static <T> T upsert(Class<T> clazz, Object data) throws Exception {
		return getOrmWrapper().upsert(clazz, data);
	}

	public static void upsertBatch(Class<?> clazz, List<?> list) throws Exception {
		getOrmWrapper().upsertBatch(clazz, list);
	}

	public static void upsert(String tableName, Object data) throws Exception {
		getOrmWrapper().upsert(tableName, data);
	}

	public static void upsertBatch(String tableName, List<?> list) throws Exception {
		getOrmWrapper().upsertBatch(tableName, list);
	}

	public static void delete(Object data) throws Exception {
		getOrmWrapper().delete(data);
	}

	public static void deleteBatch(List<?> list) throws Exception {
		getOrmWrapper().deleteBatch(list);
	}

	public static <T> T delete(Class<T> clazz, Object... pkCondition) throws Exception {
		return getOrmWrapper().delete(clazz, pkCondition);
	}

	public static void deleteBatch(Class<?> clazz, List<?> list) throws Exception {
		getOrmWrapper().deleteBatch(clazz, list);
	}

	public static <T> T deleteByCondition(T condition) throws Exception {
		return getOrmWrapper().deleteByCondition(condition);
	}

	public static <T> T deleteByCondition(Class<T> clazz, Object condition) throws Exception {
		return getOrmWrapper().deleteByCondition(clazz, condition);
	}

	public static void delete(String tableName, Object... pkCondition) throws Exception {
		getOrmWrapper().delete(tableName, pkCondition);
	}

	public static void deleteBatch(String tableName, List<?> list) throws Exception {
		getOrmWrapper().deleteBatch(tableName, list);
	}

	public static void deleteByCondition(String tableName, Object condition) throws Exception {
		getOrmWrapper().deleteByCondition(tableName, condition);
	}

	public static int selectSize(Class<?> clazz, Object condition) throws Exception {
		return getOrmWrapper().selectSize(clazz, condition);
	}

	public static <T> List<T> selectList(T condition) throws Exception {
		return getOrmWrapper().selectList(condition);
	}

	public static <T> List<T> selectList(Class<T> clazz, Object condition) throws Exception {
		return getOrmWrapper().selectList(clazz, condition);
	}

	public static <T> List<T> selectListWithLock(Class<T> clazz, Object condition) throws Exception {
		return getOrmWrapper().selectListWithLock(clazz, condition);
	}

	public static <T> Page<T> selectPage(Class<T> clazz, Query query) throws Exception {
		return getOrmWrapper().selectPage(clazz, query);
	}

	public static <T> int selectSize(String tableName, Object condition) throws Exception {
		return getOrmWrapper().selectSize(tableName, condition);
	}

	public static <T> List<T> selectList(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectList(tableName, condition, requiredType);
	}

	public static <T> List<T> selectListWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectListWithLock(tableName, condition, requiredType);
	}

	public static <T> Page<T> selectPage(String tableName, Query query, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectPage(tableName, query, requiredType);
	}

	public static <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListByQl(ql, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectListByQl(ql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageByQl(ql, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectPageByQl(ql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeByQl(String ql, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().selectSizeByQl(ql, paramMap);
	}

	public static <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListByQlPath(qlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectListByQlPath(qlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageByQlPath(qlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectPageByQlPath(qlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().selectSizeByQlPath(qlPath, paramMap);
	}

	public static <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListBySql(sql, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectListBySql(sql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageBySql(sql, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectPageBySql(sql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeBySql(String sql, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().selectSizeBySql(sql, paramMap);
	}

	public static <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getOrmWrapper().selectListBySqlPath(sqlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectListBySqlPath(sqlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageBySqlPath(sqlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getOrmWrapper().selectPageBySqlPath(sqlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().selectSizeBySqlPath(sqlPath, paramMap);
	}

	public static <T> int deleteList(T condition) throws Exception {
		return getOrmWrapper().deleteList(condition);
	}

	public static int deleteList(Class<?> clazz, Object condition) throws Exception {
		return getOrmWrapper().deleteList(clazz, condition);
	}

	public static int executeByQl(String ql, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().executeByQl(ql, paramMap);
	}

	public static int executeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().executeByQlPath(qlPath, paramMap);
	}

	public static int executeBySql(String sql, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().executeBySql(sql, paramMap);
	}

	public static int executeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception {
		return getOrmWrapper().executeBySqlPath(sqlPath, paramMap);
	}
}