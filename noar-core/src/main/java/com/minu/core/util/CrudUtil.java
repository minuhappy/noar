package com.minu.core.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.minu.core.dao.Dao;

/**
 * Entity에 대한 Create, Update, Delete, Search, Find 등의 공통 함수 유틸리티
 * 
 * @author Minu.Kim
 */
@Service
@DependsOn("jpaDaoHandler")
public class CrudUtil {
	private static Dao dao;

	@Autowired
	@Qualifier("jpaDaoHandler")
	private void dao(Dao dao) {
		CrudUtil.dao = dao;
	}

	/**
	 * Entity의 Key 리턴
	 * 
	 * @param entity
	 * @return
	 */
	public static Object getKey(Object entity) {
		return dao.getKey(entity);
	}

	/**
	 * Entity 생성
	 * 
	 * @param input
	 * @return
	 */
	public static void create(Object entity) {
		dao.create(entity);
	}

	/**
	 * ID로 Entity 조회
	 * 
	 * @param input
	 * @return
	 */
	public static <T> T read(T entity) {
		return dao.read(entity);
	}

	public static <T> T read(Class<T> entity, Object key) {
		return dao.read(entity, key);
	}

	/**
	 * Entity 업데이트
	 * 
	 * @param input
	 * @return
	 */
	public static Object update(Object entity) {
		return dao.update(entity);
	}

	public static <T> T update(Class<T> entity, Object key) {
		return dao.update(entity, key);
	}

	/**
	 * Entity 삭제
	 * 
	 * @param input
	 * @return
	 */
	public static <T> void delete(T entity) {
		dao.delete(entity);
	}

	public static <T> void delete(Class<T> entity, Object key) {
		dao.delete(entity, key);
	}

	/**
	 * Entity 모든 검색 조건 처리
	 * 
	 * @param entityClass
	 * @param countPerPage
	 * @param offset
	 * @param searchConds
	 * @param sortConds
	 * @return
	 */
	// public static <E> PageOutput search(Class<?> entityClass, int
	// countPerPage, int
	// offset, List<SearchCondition> searchConds,
	// List<SortCondition> sortConds) {
	// }
}