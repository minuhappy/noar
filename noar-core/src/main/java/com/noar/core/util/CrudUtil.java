package com.noar.core.util;

import java.util.List;
import java.util.Map;

import com.noar.common.util.BeanUtil;
import com.noar.core.orm.wrapper.DbistWrapper;
import com.noar.core.orm.wrapper.IOrmWrapper;
import com.noar.dbist.dml.Page;
import com.noar.dbist.dml.Query;

/**
 * Entity에 대한 Create, Update, Delete, Search, Find 등의 공통 함수 유틸리티
 * 
 * @author Minu.Kim
 */
public class CrudUtil {

	private static IOrmWrapper ormWrapper;

	static {
		ormWrapper = BeanUtil.get(DbistWrapper.class);
	}
	
	// private static IOrmWrapper getOrmWrapper() {
	// if (ormWrapper == null) {
	// ormWrapper = BeanUtil.get(DbistWrapper.class);
	// }
	// return ormWrapper;
	// }

	public static <T> T select(T data) throws Exception {
		return ormWrapper.select(data);
	}

	public static <T> T selectWithLock(T data) throws Exception {
		return ormWrapper.selectWithLock(data);
	}

	public static <T> T select(Class<T> clazz, Object... pkCondition) throws Exception {
		return ormWrapper.select(clazz, pkCondition);
	}

	public static <T> T selectWithLock(Class<T> clazz, Object... pkCondition) throws Exception {
		return ormWrapper.select(clazz, pkCondition);
	}

	public static <T> T selectByCondition(T data) throws Exception {
		return ormWrapper.selectByCondition(data);
	}

	public static <T> T selectByCondition(Class<T> clazz, T condition) throws Exception {
		return ormWrapper.selectByCondition(clazz, condition);
	}

	public static <T> T selectByConditionWithLock(Class<T> clazz, Object condition) throws Exception {
		return ormWrapper.selectByConditionWithLock(clazz, condition);
	}

	public static <T> T select(String tableName, Object pkCondition, Class<T> requiredType) throws Exception {
		return ormWrapper.select(tableName, pkCondition, requiredType);
	}

	public static <T> T selectWithLock(String tableName, Object pkCondition, Class<T> requiredType) throws Exception {
		return ormWrapper.selectWithLock(tableName, pkCondition, requiredType);
	}

	public static <T> T selectByCondition(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return ormWrapper.selectByCondition(tableName, condition, requiredType);
	}

	public static <T> T selectByConditionWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return ormWrapper.selectByConditionWithLock(tableName, condition, requiredType);
	}

	public static <T> T selectByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return ormWrapper.selectByQl(ql, paramMap, requiredType);
	}

	public static <T> T selectByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return ormWrapper.selectByQlPath(qlPath, paramMap, requiredType);
	}

	public static <T> T selectBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return ormWrapper.selectBySql(sql, paramMap, requiredType);
	}

	public static <T> T selectBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return ormWrapper.selectBySqlPath(sqlPath, paramMap, requiredType);
	}

	public static void insert(Object data) throws Exception {
		ormWrapper.insert(data);
	}

	public static void insertBatch(List<?> list) throws Exception {
		ormWrapper.insertBatch(list);
	}

	public static <T> T insert(Class<T> clazz, Object data) throws Exception {
		return ormWrapper.insert(clazz, data);
	}

	public static void insertBatch(Class<?> clazz, List<?> list) throws Exception {
		ormWrapper.insertBatch(clazz, list);
	}

	public static void insert(String tableName, Object data) throws Exception {
		ormWrapper.insert(data);
	}

	public static void insertBatch(String tableName, List<?> list) throws Exception {
		ormWrapper.insertBatch(tableName, list);
	}

	public static void update(Object data) throws Exception {
		ormWrapper.update(data);
	}

	public static void updateBatch(List<?> list) throws Exception {
		ormWrapper.updateBatch(list);
	}

	public static void update(Object data, String... fieldNames) throws Exception {
		ormWrapper.update(data, fieldNames);
	}

	public static void updateBatch(List<?> list, String... fieldNames) throws Exception {
		ormWrapper.updateBatch(list, fieldNames);
	}

	public static <T> T update(Class<T> clazz, Object data) throws Exception {
		return ormWrapper.update(clazz, data);
	}

	public static <T> T update(Class<T> clazz, Object data, String... fieldNames) throws Exception {
		return ormWrapper.update(clazz, data, fieldNames);
	}

	public static void update(String tableName, Object data) throws Exception {
		ormWrapper.update(tableName, data);
	}

	public static void updateBatch(String tableName, List<?> list) throws Exception {
		ormWrapper.updateBatch(tableName, list);
	}

	public static void update(String tableName, Object data, String... fieldNames) throws Exception {
		ormWrapper.update(tableName, data, fieldNames);
	}

	public static void updateBatch(String tableName, List<?> list, String... fieldNames) throws Exception {
		ormWrapper.updateBatch(tableName, list, fieldNames);
	}

	public static void upsert(Object data) throws Exception {
		ormWrapper.upsert(data);
	}

	public static void upsertBatch(List<?> list) throws Exception {
		ormWrapper.upsertBatch(list);
	}

	public static <T> T upsert(Class<T> clazz, Object data) throws Exception {
		return ormWrapper.upsert(clazz, data);
	}

	public static void upsertBatch(Class<?> clazz, List<?> list) throws Exception {
		ormWrapper.upsertBatch(clazz, list);
	}

	public static void upsert(String tableName, Object data) throws Exception {
		ormWrapper.upsert(tableName, data);
	}

	public static void upsertBatch(String tableName, List<?> list) throws Exception {
		ormWrapper.upsertBatch(tableName, list);
	}

	public static void delete(Object data) throws Exception {
		ormWrapper.delete(data);
	}

	public static void deleteBatch(List<?> list) throws Exception {
		ormWrapper.deleteBatch(list);
	}

	public static <T> T delete(Class<T> clazz, Object... pkCondition) throws Exception {
		return ormWrapper.delete(clazz, pkCondition);
	}

	public static void deleteBatch(Class<?> clazz, List<?> list) throws Exception {
		ormWrapper.deleteBatch(clazz, list);
	}

	public static <T> T deleteByCondition(T condition) throws Exception {
		return ormWrapper.deleteByCondition(condition);
	}

	public static <T> T deleteByCondition(Class<T> clazz, Object condition) throws Exception {
		return ormWrapper.deleteByCondition(clazz, condition);
	}

	public static void delete(String tableName, Object... pkCondition) throws Exception {
		ormWrapper.delete(tableName, pkCondition);
	}

	public static void deleteBatch(String tableName, List<?> list) throws Exception {
		ormWrapper.deleteBatch(tableName, list);
	}

	public static void deleteByCondition(String tableName, Object condition) throws Exception {
		ormWrapper.deleteByCondition(tableName, condition);
	}

	public static int selectSize(Class<?> clazz, Object condition) throws Exception {
		return ormWrapper.selectSize(clazz, condition);
	}

	public static <T> List<T> selectList(T condition) throws Exception {
		return ormWrapper.selectList(condition);
	}

	public static <T> List<T> selectList(Class<T> clazz, Object condition) throws Exception {
		return ormWrapper.selectList(clazz, condition);
	}

	public static <T> List<T> selectListWithLock(Class<T> clazz, Object condition) throws Exception {
		return ormWrapper.selectListWithLock(clazz, condition);
	}

	public static <T> Page<T> selectPage(Class<T> clazz, Query query) throws Exception {
		return ormWrapper.selectPage(clazz, query);
	}

	public static <T> int selectSize(String tableName, Object condition) throws Exception {
		return ormWrapper.selectSize(tableName, condition);
	}

	public static <T> List<T> selectList(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return ormWrapper.selectList(tableName, condition, requiredType);
	}

	public static <T> List<T> selectListWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return ormWrapper.selectListWithLock(tableName, condition, requiredType);
	}

	public static <T> Page<T> selectPage(String tableName, Query query, Class<T> requiredType) throws Exception {
		return ormWrapper.selectPage(tableName, query, requiredType);
	}

	public static <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListByQl(ql, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectListByQl(ql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageByQl(ql, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectPageByQl(ql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeByQl(String ql, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.selectSizeByQl(ql, paramMap);
	}

	public static <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListByQlPath(qlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectListByQlPath(qlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageByQlPath(qlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectPageByQlPath(qlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.selectSizeByQlPath(qlPath, paramMap);
	}

	public static <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListBySql(sql, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectListBySql(sql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageBySql(sql, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectPageBySql(sql, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeBySql(String sql, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.selectSizeBySql(sql, paramMap);
	}

	public static <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return ormWrapper.selectListBySqlPath(sqlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectListBySqlPath(sqlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageBySqlPath(sqlPath, paramMap, requiredType, 0, 0);
	}

	public static <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return ormWrapper.selectPageBySqlPath(sqlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public static int selectSizeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.selectSizeBySqlPath(sqlPath, paramMap);
	}

	public static <T> int deleteList(T condition) throws Exception {
		return ormWrapper.deleteList(condition);
	}

	public static int deleteList(Class<?> clazz, Object condition) throws Exception {
		return ormWrapper.deleteList(clazz, condition);
	}

	public static int executeByQl(String ql, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.executeByQl(ql, paramMap);
	}

	public static int executeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.executeByQlPath(qlPath, paramMap);
	}

	public static int executeBySql(String sql, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.executeBySql(sql, paramMap);
	}

	public static int executeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception {
		return ormWrapper.executeBySqlPath(sqlPath, paramMap);
	}
}