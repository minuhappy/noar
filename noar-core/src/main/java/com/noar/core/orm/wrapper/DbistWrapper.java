package com.noar.core.orm.wrapper;

import java.util.List;
import java.util.Map;

import com.noar.common.util.BeanUtil;
import com.noar.dbist.dml.Dml;
import com.noar.dbist.dml.Page;
import com.noar.dbist.dml.Query;

public class DbistWrapper implements IOrmWrapper {
	private Dml dml;

	private Dml getDml() {
		if (dml == null) {
			dml = BeanUtil.get(Dml.class);
		}
		return dml;
	}

	public <T> T select(T data) throws Exception {
		return getDml().select(data);
	}

	public <T> T selectWithLock(T data) throws Exception {
		return getDml().selectWithLock(data);
	}

	public <T> T select(Class<T> clazz, Object... pkCondition) throws Exception {
		return getDml().select(clazz, pkCondition);
	}

	public <T> T selectWithLock(Class<T> clazz, Object... pkCondition) throws Exception {
		return getDml().select(clazz, pkCondition);
	}

	@SuppressWarnings("unchecked")
	public <T> T selectByCondition(T data) throws Exception {
		Class<T> clazz = (Class<T>) data.getClass();
		return (T) getDml().selectByCondition(clazz, data);
	}

	public <T> T selectByCondition(Class<T> clazz, T condition) throws Exception {
		return getDml().selectByCondition(clazz, condition);
	}

	public <T> T selectByConditionWithLock(Class<T> clazz, Object condition) throws Exception {
		return getDml().selectByConditionWithLock(clazz, condition);
	}

	public <T> T select(String tableName, Object pkCondition, Class<T> requiredType) throws Exception {
		return getDml().select(tableName, pkCondition, requiredType);
	}

	public <T> T selectWithLock(String tableName, Object pkCondition, Class<T> requiredType) throws Exception {
		return getDml().selectWithLock(tableName, pkCondition, requiredType);
	}

	public <T> T selectByCondition(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getDml().selectByCondition(tableName, condition, requiredType);
	}

	public <T> T selectByConditionWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getDml().selectByConditionWithLock(tableName, condition, requiredType);
	}

	public <T> T selectByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getDml().selectByQl(ql, paramMap, requiredType);
	}

	public <T> T selectByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getDml().selectByQlPath(qlPath, paramMap, requiredType);
	}

	public <T> T selectBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getDml().selectBySql(sql, paramMap, requiredType);
	}

	public <T> T selectBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getDml().selectBySqlPath(sqlPath, paramMap, requiredType);
	}

	public void insert(Object data) throws Exception {
		getDml().insert(data);
	}

	public void insertBatch(List<?> list) throws Exception {
		getDml().insertBatch(list);
	}

	public <T> T insert(Class<T> clazz, Object data) throws Exception {
		return getDml().insert(clazz, data);
	}

	public void insertBatch(Class<?> clazz, List<?> list) throws Exception {
		getDml().insertBatch(clazz, list);
	}

	public void insert(String tableName, Object data) throws Exception {
		getDml().insert(data);
	}

	public void insertBatch(String tableName, List<?> list) throws Exception {
		getDml().insertBatch(tableName, list);
	}

	public void update(Object data) throws Exception {
		getDml().update(data);
	}

	public void updateBatch(List<?> list) throws Exception {
		getDml().updateBatch(list);
	}

	public void update(Object data, String... fieldNames) throws Exception {
		getDml().update(data, fieldNames);
	}

	public void updateBatch(List<?> list, String... fieldNames) throws Exception {
		getDml().updateBatch(list, fieldNames);
	}

	public <T> T update(Class<T> clazz, Object data) throws Exception {
		return getDml().update(clazz, data);
	}

	public <T> T update(Class<T> clazz, Object data, String... fieldNames) throws Exception {
		return getDml().update(clazz, data, fieldNames);
	}

	public void update(String tableName, Object data) throws Exception {
		getDml().update(tableName, data);
	}

	public void updateBatch(String tableName, List<?> list) throws Exception {
		getDml().updateBatch(tableName, list);
	}

	public void update(String tableName, Object data, String... fieldNames) throws Exception {
		getDml().update(tableName, data, fieldNames);
	}

	public void updateBatch(String tableName, List<?> list, String... fieldNames) throws Exception {
		getDml().updateBatch(tableName, list, fieldNames);
	}

	public void upsert(Object data) throws Exception {
		getDml().upsert(data);
	}

	public void upsertBatch(List<?> list) throws Exception {
		getDml().upsertBatch(list);
	}

	public <T> T upsert(Class<T> clazz, Object data) throws Exception {
		return getDml().upsert(clazz, data);
	}

	public void upsertBatch(Class<?> clazz, List<?> list) throws Exception {
		getDml().upsertBatch(clazz, list);
	}

	public void upsert(String tableName, Object data) throws Exception {
		getDml().upsert(tableName, data);
	}

	public void upsertBatch(String tableName, List<?> list) throws Exception {
		getDml().upsertBatch(tableName, list);
	}

	public void delete(Object data) throws Exception {
		getDml().delete(data);
	}

	public void deleteBatch(List<?> list) throws Exception {
		getDml().deleteBatch(list);
	}

	public <T> T delete(Class<T> clazz, Object... pkCondition) throws Exception {
		return getDml().delete(clazz, pkCondition);
	}

	public void deleteBatch(Class<?> clazz, List<?> list) throws Exception {
		getDml().deleteBatch(clazz, list);
	}

	@SuppressWarnings("unchecked")
	public <T> T deleteByCondition(T condition) throws Exception {
		Class<T> clazz = (Class<T>) condition.getClass();
		return (T) getDml().deleteByCondition(clazz, condition);
	}

	public <T> T deleteByCondition(Class<T> clazz, Object condition) throws Exception {
		return getDml().deleteByCondition(clazz, condition);
	}

	public void delete(String tableName, Object... pkCondition) throws Exception {
		getDml().delete(tableName, pkCondition);
	}

	public void deleteBatch(String tableName, List<?> list) throws Exception {
		getDml().deleteBatch(tableName, list);
	}

	public void deleteByCondition(String tableName, Object condition) throws Exception {
		getDml().deleteByCondition(tableName, condition);
	}

	public int selectSize(Class<?> clazz, Object condition) throws Exception {
		return getDml().selectSize(clazz, condition);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(T condition) throws Exception {
		Class<T> clazz = (Class<T>) condition.getClass();
		return getDml().selectList(clazz, condition);
	}

	public <T> List<T> selectList(Class<T> clazz, Object condition) throws Exception {
		return getDml().selectList(clazz, condition);
	}

	public <T> List<T> selectListWithLock(Class<T> clazz, Object condition) throws Exception {
		return getDml().selectListWithLock(clazz, condition);
	}

	public <T> Page<T> selectPage(Class<T> clazz, Query query) throws Exception {
		return getDml().selectPage(clazz, query);
	}

	public <T> int selectSize(String tableName, Object condition) throws Exception {
		return getDml().selectSize(tableName, condition);
	}

	public <T> List<T> selectList(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getDml().selectList(tableName, condition, requiredType);
	}

	public <T> List<T> selectListWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception {
		return getDml().selectListWithLock(tableName, condition, requiredType);
	}

	public <T> Page<T> selectPage(String tableName, Query query, Class<T> requiredType) throws Exception {
		return getDml().selectPage(tableName, query, requiredType);
	}

	public <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListByQl(ql, paramMap, requiredType, 0, 0);
	}

	public <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectListByQl(ql, paramMap, requiredType, pageIndex, pageSize);
	}

	public <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageByQl(ql, paramMap, requiredType, 0, 0);
	}

	public <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectPageByQl(ql, paramMap, requiredType, pageIndex, pageSize);
	}

	public int selectSizeByQl(String ql, Map<String, ?> paramMap) throws Exception {
		return getDml().selectSizeByQl(ql, paramMap);
	}

	public <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListByQlPath(qlPath, paramMap, requiredType, 0, 0);
	}

	public <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectListByQlPath(qlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageByQlPath(qlPath, paramMap, requiredType, 0, 0);
	}

	public <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectPageByQlPath(qlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public int selectSizeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception {
		return getDml().selectSizeByQlPath(qlPath, paramMap);
	}

	public <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectListBySql(sql, paramMap, requiredType, 0, 0);
	}

	public <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectListBySql(sql, paramMap, requiredType, pageIndex, pageSize);
	}

	public <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageBySql(sql, paramMap, requiredType, 0, 0);
	}

	public <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectPageBySql(sql, paramMap, requiredType, pageIndex, pageSize);
	}

	public int selectSizeBySql(String sql, Map<String, ?> paramMap) throws Exception {
		return getDml().selectSizeBySql(sql, paramMap);
	}

	public <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return getDml().selectListBySqlPath(sqlPath, paramMap, requiredType, 0, 0);
	}

	public <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectListBySqlPath(sqlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception {
		return selectPageBySqlPath(sqlPath, paramMap, requiredType, 0, 0);
	}

	public <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception {
		return getDml().selectPageBySqlPath(sqlPath, paramMap, requiredType, pageIndex, pageSize);
	}

	public int selectSizeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception {
		return getDml().selectSizeBySqlPath(sqlPath, paramMap);
	}

	@SuppressWarnings("unchecked")
	public <T> int deleteList(T condition) throws Exception {
		Class<T> clazz = (Class<T>) condition.getClass();
		return getDml().deleteList(clazz, condition);
	}

	public int deleteList(Class<?> clazz, Object condition) throws Exception {
		return getDml().deleteList(clazz, condition);
	}

	public int executeByQl(String ql, Map<String, ?> paramMap) throws Exception {
		return getDml().executeByQl(ql, paramMap);
	}

	public int executeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception {
		return getDml().executeByQlPath(qlPath, paramMap);
	}

	public int executeBySql(String sql, Map<String, ?> paramMap) throws Exception {
		return getDml().executeBySql(sql, paramMap);
	}

	public int executeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception {
		return getDml().executeBySqlPath(sqlPath, paramMap);
	}
}
