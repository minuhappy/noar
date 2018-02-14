package com.noar.core.orm.wrapper;

import java.util.List;
import java.util.Map;

import com.noar.dbist.dml.Page;
import com.noar.dbist.dml.Query;

public interface IOrmWrapper {

	public <T> T select(T data) throws Exception;

	public <T> T selectWithLock(T data) throws Exception;

	public <T> T select(Class<T> clazz, Object... pkCondition) throws Exception;

	public <T> T selectWithLock(Class<T> clazz, Object... pkCondition) throws Exception;

	public <T> T selectByCondition(T data) throws Exception;

	public <T> T selectByCondition(Class<T> clazz, T condition) throws Exception;

	public <T> T selectByConditionWithLock(Class<T> clazz, Object condition) throws Exception;

	public <T> T select(String tableName, Object pkCondition, Class<T> requiredType) throws Exception;

	public <T> T selectWithLock(String tableName, Object pkCondition, Class<T> requiredType) throws Exception;

	public <T> T selectByCondition(String tableName, Object condition, Class<T> requiredType) throws Exception;

	public <T> T selectByConditionWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception;

	public <T> T selectByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> T selectByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> T selectBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> T selectBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public void insert(Object data) throws Exception;

	public void insertBatch(List<?> list) throws Exception;

	public <T> T insert(Class<T> clazz, Object data) throws Exception;

	public void insertBatch(Class<?> clazz, List<?> list) throws Exception;

	public void insert(String tableName, Object data) throws Exception;

	public void insertBatch(String tableName, List<?> list) throws Exception;

	public void update(Object data) throws Exception;

	public void updateBatch(List<?> list) throws Exception;

	public void update(Object data, String... fieldNames) throws Exception;

	public void updateBatch(List<?> list, String... fieldNames) throws Exception;

	public <T> T update(Class<T> clazz, Object data) throws Exception;

	public <T> T update(Class<T> clazz, Object data, String... fieldNames) throws Exception;

	public void update(String tableName, Object data) throws Exception;

	public void updateBatch(String tableName, List<?> list) throws Exception;

	public void update(String tableName, Object data, String... fieldNames) throws Exception;

	public void updateBatch(String tableName, List<?> list, String... fieldNames) throws Exception;

	public void upsert(Object data) throws Exception;

	public void upsertBatch(List<?> list) throws Exception;

	public <T> T upsert(Class<T> clazz, Object data) throws Exception;

	public void upsertBatch(Class<?> clazz, List<?> list) throws Exception;

	public void upsert(String tableName, Object data) throws Exception;

	public void upsertBatch(String tableName, List<?> list) throws Exception;

	public void delete(Object data) throws Exception;

	public void deleteBatch(List<?> list) throws Exception;

	public <T> T delete(Class<T> clazz, Object... pkCondition) throws Exception;

	public void deleteBatch(Class<?> clazz, List<?> list) throws Exception;

	public <T> T deleteByCondition(T condition) throws Exception;

	public <T> T deleteByCondition(Class<T> clazz, Object condition) throws Exception;

	public void delete(String tableName, Object... pkCondition) throws Exception;

	public void deleteBatch(String tableName, List<?> list) throws Exception;

	public void deleteByCondition(String tableName, Object condition) throws Exception;

	public int selectSize(Class<?> clazz, Object condition) throws Exception;

	public <T> List<T> selectList(T condition) throws Exception;

	public <T> List<T> selectList(Class<T> clazz, Object condition) throws Exception;

	public <T> List<T> selectListWithLock(Class<T> clazz, Object condition) throws Exception;

	public <T> Page<T> selectPage(Class<T> clazz, Query query) throws Exception;

	public <T> int selectSize(String tableName, Object condition) throws Exception;

	public <T> List<T> selectList(String tableName, Object condition, Class<T> requiredType) throws Exception;

	public <T> List<T> selectListWithLock(String tableName, Object condition, Class<T> requiredType) throws Exception;

	public <T> Page<T> selectPage(String tableName, Query query, Class<T> requiredType) throws Exception;

	public <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> List<T> selectListByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> Page<T> selectPageByQl(String ql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public int selectSizeByQl(String ql, Map<String, ?> paramMap) throws Exception;

	public <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> List<T> selectListByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> Page<T> selectPageByQlPath(String qlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public int selectSizeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception;

	public <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> List<T> selectListBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> Page<T> selectPageBySql(String sql, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public int selectSizeBySql(String sql, Map<String, ?> paramMap) throws Exception;

	public <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> List<T> selectListBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType) throws Exception;

	public <T> Page<T> selectPageBySqlPath(String sqlPath, Map<String, ?> paramMap, Class<T> requiredType, int pageIndex, int pageSize) throws Exception;

	public int selectSizeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception;

	public <T> int deleteList(T condition) throws Exception;

	public int deleteList(Class<?> clazz, Object condition) throws Exception;

	public int executeByQl(String ql, Map<String, ?> paramMap) throws Exception;

	public int executeByQlPath(String qlPath, Map<String, ?> paramMap) throws Exception;

	public int executeBySql(String sql, Map<String, ?> paramMap) throws Exception;

	public int executeBySqlPath(String sqlPath, Map<String, ?> paramMap) throws Exception;
}
