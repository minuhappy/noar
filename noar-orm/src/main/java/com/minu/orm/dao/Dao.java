/**
 * 
 */
package com.minu.orm.dao;

import java.util.List;
import java.util.Map;

import com.minu.orm.model.Page;
import com.minu.orm.model.SearchCondition;
import com.minu.orm.model.SortCondition;

/**
 * @author Administrator
 *
 */
public interface Dao {

	/**
	 * Entity의 Key 리턴
	 * 
	 * @param entity
	 * @return
	 */
	public Object getKey(Object entity);

	/**
	 * Entity 생성
	 * 
	 * @param input
	 * @return
	 */
	public void create(Object entity);

	/**
	 * ID로 Entity 조회
	 * 
	 * @param input
	 * @return
	 */
	public <T> T read(T entity);

	public <T> T read(Class<T> entity, Object... key);

	/**
	 * Entity 업데이트
	 * 
	 * @param input
	 * @return
	 */
	public <T> T update(T entity);

	public <T> T update(Class<T> entityClass, Object key);

	/**
	 * Entity 삭제
	 * 
	 * @param input
	 * @return
	 */
	public void delete(Object entity);

	public <T> void delete(Class<T> entity, Object key);

	/**
	 * 검색 결과를 List 형태로 반환.
	 * 
	 * @param entity
	 * @return
	 */
	public <T> List<T> list(T entity);

	public <T> List<T> list(Class<T> entityClass, List<SearchCondition> searchConds);

	public <T> List<T> list(Class<T> entityClass, List<SearchCondition> searchConds, List<SortCondition> sortConds);

	public <T> List<T> listByPath(String path, Class<T> clazz, Map<String, Object> paramMap);

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
	public <T> Page<T> search(T entity, int countPage, int offset);

	public <T> Page<T> search(Class<T> entityClass, int countPage, int offset, List<SearchCondition> searchConds,
			List<SortCondition> sortConds);
}
