package com.minu.core.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.minu.core.exception.ServiceException;
import com.minu.core.model.Page;
import com.minu.core.model.SearchCondition;
import com.minu.core.model.SortCondition;
import com.minu.core.system.Constants;
import com.minu.core.system.processor.VelocityProcessor;
import com.minu.core.util.ValueUtil;

@Service
@Qualifier("jpaDaoHandler")
public class JpaDaoHandler implements Dao {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private VelocityProcessor processor;
	
	/**
	 * Logger
	 */
	protected Logger logger = LoggerFactory.getLogger(JpaDaoHandler.class);

	/**
	 * Entity의 Key 리턴
	 * 
	 * @param entity
	 * @return
	 */
	public Object getKey(Object entity) {
		return entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
	}

	/**
	 * Entity 생성
	 * 
	 * @param input
	 * @return
	 */
	public void create(Object entity) {
		this.entityManager.persist(entity);
	}

	/**
	 * ID로 Entity 조회
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T read(T entity) {
		return (T) read(entity.getClass(), this.getKey(entity));
	}

	public <T> T read(Class<T> entity, Object... key) {
		return entityManager.find(entity, key);
	}

	/**
	 * Entity 업데이트
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T update(T entity) {
		return (T) update(entity.getClass(), this.getKey(entity));
	}

	public <T> T update(Class<T> entityClass, Object key) {
		return entityManager.find(entityClass, key);
	}

	/**
	 * Entity 삭제
	 * 
	 * @param input
	 * @return
	 */
	public void delete(Object entity) {
		delete(entity.getClass(), this.getKey(entity));
	}

	public <T> void delete(Class<T> entity, Object key) {
		Object data = entityManager.find(entity, key);

		if (data == null) {
			throw new ServiceException("Record Not Found (key:" + key + ")", null);
		}

		entityManager.remove(data);
	}

	/**
	 * 검색 결과를 List 형태로 반환.
	 * 
	 * @param entityClass
	 * @param selectColumns
	 * @param searchConds
	 * @param sortConds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(T entity) {
		List<SearchCondition> searchConditionList = new ArrayList<>();
		Field[] fields = entity.getClass().getDeclaredFields();

		for (Field field : fields) {
			Object value = ValueUtil.getFieldValue(entity, field.getName());
			if (ValueUtil.isNotEmpty(value)) {
				searchConditionList.add(new SearchCondition(field.getName(), Constants.EQUAL, value));
			}
		}
		return (List<T>) list(entity.getClass(), searchConditionList);
	}

	public <T> List<T> list(Class<T> entityClass, List<SearchCondition> searchConds) {
		return list(entityClass, searchConds, null);
	}

	public <T> List<T> list(Class<T> entityClass, List<SearchCondition> searchConds, List<SortCondition> sortConds) {
		// Criteria Builder
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		// Query
		CriteriaQuery<T> criteriaQuery = cb.createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);

		// Setup Criteria Query By Search Conditions & Order Conditions
		setupCriteriaQuery(cb, root, criteriaQuery, searchConds, sortConds);

		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		List<T> resultList = query.getResultList();

		return resultList;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> listByPath(String path, Class<T> clazz, Map<String, Object> paramMap) {
		String value = ValueUtil.getValueByFilePath(path);
		String sql = processor.process(value, paramMap);
		Query query = entityManager.createNativeQuery(sql, clazz);

		Set<String> param = paramMap.keySet();
		for (String key : param) {
			query.setParameter(key, paramMap.get(key));
		}
		return query.getResultList();
	}

	/**
	 * Entity 모든 검색 조건 처리
	 * 
	 * @param entityClass
	 * @param countPage
	 * @param offset
	 * @param searchConds
	 * @param sortConds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Page<T> search(T entity, int countPage, int offset) {
		List<SearchCondition> searchConditionList = new ArrayList<>();
		Field[] fields = entity.getClass().getDeclaredFields();

		for (Field field : fields) {
			Object value = ValueUtil.getFieldValue(entity, field.getName());
			if (ValueUtil.isNotEmpty(value)) {
				searchConditionList.add(new SearchCondition(field.getName(), Constants.EQUAL, value));
			}
		}
		return (Page<T>) search(entity.getClass(), countPage, offset);
	}

	public <T> Page<T> search(Class<T> entityClass, int countPage, int offset, List<SearchCondition> searchConds,
			List<SortCondition> sortConds) {
		// Criteria Builder
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		// Total Count Query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		// Query
		CriteriaQuery<T> criteriaQuery = cb.createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);

		if (ValueUtil.isNotEmpty(searchConds) || ValueUtil.isNotEmpty(sortConds)) {
			// Setup Criteria Query By Search Conditions & Order Conditions
			setupCriteriaQuery(cb, root, criteriaQuery, searchConds, sortConds);
			setupCriteriaQuery(cb, root, countQuery, searchConds, null);
		}

		countQuery.select(cb.count(countQuery.from(entityClass)));
		Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

		// For Pagination
		TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
		query.setFirstResult(offset);
		query.setMaxResults(countPage);
		List<T> list = query.getResultList();

		Page<T> page = new Page<T>();
		page.setList(list);
		page.setTotalCount(totalCount);

		long totalPage = (long) Math.ceil((double) totalCount / offset);
		page.setTotalPage(totalPage);

		return page;
	}

	/**
	 * setup criteria query by search conditions or sort conditions
	 *
	 * @param cb
	 * @param root
	 * @param criteriaQuery
	 * @param input
	 */
	private void setupCriteriaQuery(CriteriaBuilder cb, Root<?> root, CriteriaQuery<?> criteriaQuery,
			List<SearchCondition> searchConds, List<SortCondition> sortConds) {
		List<Predicate> predicateList = new ArrayList<Predicate>();
		List<Order> orderList = new ArrayList<Order>();

		if (ValueUtil.isNotEmpty(searchConds)) {
			for (SearchCondition cond : searchConds) {
				Predicate p = getWhereCondition(cb, root, cond);
				if (p != null) {
					predicateList.add(p);
				}
			}
		}

		if (ValueUtil.isNotEmpty(sortConds)) {
			for (SortCondition cond : sortConds) {
				Order o = getOrderCondition(cb, root, cond);
				if (o != null) {
					orderList.add(o);
				}
			}
		}

		criteriaQuery.multiselect(root);
		// criteriaQuery.select(root);

		if (ValueUtil.isNotEmpty(predicateList)) {
			int size = predicateList.size();
			Predicate[] predicates = new Predicate[predicateList.size()];

			for (int i = 0; i < size; i++) {
				predicates[i] = predicateList.get(i);
			}

			criteriaQuery.where(predicates);
		}

		if (ValueUtil.isNotEmpty(orderList)) {
			int size = orderList.size();
			Order[] orders = new Order[size];

			for (int i = 0; i < size; i++) {
				orders[i] = orderList.get(i);
			}

			criteriaQuery.orderBy(orders);
		}
	}

	/**
	 * Get Where Condition
	 *
	 * @param cb
	 * @param root
	 * @param cond
	 * @return
	 */
	private Predicate getWhereCondition(CriteriaBuilder cb, Root<?> root, SearchCondition cond) {
		String name = cond.getName();
		String operator = cond.getOperator();
		Object value = cond.getValue();
		boolean isEmpty = ValueUtil.isEmpty(value);
		Predicate p = null;

		switch (operator) {
		// 'equal' 연산자 : '='
		case Constants.EQUAL: {
			if (!isEmpty) {
				p = cb.equal(root.get(name), value);
			}
			break;
		}

		// 'notequal' 연산자 : '!='
		case Constants.NOT_EQUAL: {
			if (!isEmpty) {
				p = cb.notEqual(root.get(name), value);
			}
			break;
		}

		// 'like' 연산자 : 'like'
		case Constants.LIKE: {
			if (!isEmpty) {
				p = cb.like(root.get(name), "%" + value + "%");
			}
			break;
		}

		// 'starts with' 연산자 : 'like'
		case Constants.STARTS_WITH: {
			if (!isEmpty) {
				p = cb.like(root.get(name), value + "%");
			}
			break;
		}

		// 'ends with' 연산자 : 'like'
		case Constants.ENDS_WITH: {
			if (!isEmpty) {
				p = cb.like(root.get(name), "%" + value);
			}
			break;
		}

		// 'not like' 연산자 : 'notlike'
		case Constants.NOT_LIKE: {
			if (!isEmpty) {
				p = cb.notLike(root.get(name), "%" + value + "%");
			}
			break;
		}

		// 'does not start with' 연산자 : 'notlike'
		case Constants.DOES_NOT_START_WITH: {
			p = cb.notLike(root.get(name), value + "%");
			break;
		}

		// 'does not end with' 연산자 : 'notlike'
		case Constants.DOES_NOT_END_WITH: {
			if (!isEmpty) {
				p = cb.notLike(root.get(name), "%" + value);
			}
			break;
		}

		// // 'greater than' 연산자 : '>'
		// case Constants.GREATER_THAN: {
		// if (!isEmpty) {
		// if (type.equalsIgnoreCase("number")) {
		// p = cb.greaterThan(root.<Long> get(name), (Long) value);
		// } else {
		// cb.greaterThan(root.<String> get(name), (String) value);
		// }
		// }
		// break;
		// }
		//
		// // 'greater than equal' 연산자 : '>='
		// case Constants.GREATER_THAN_EQUAL: {
		// if (!isEmpty) {
		// if (type.equalsIgnoreCase("number")) {
		// p = cb.greaterThanOrEqualTo(root.<Long> get(name), (Long) value);
		// } else {
		// cb.greaterThanOrEqualTo(root.<String> get(name), (String) value);
		// }
		// }
		// break;
		// }
		//
		// // 'less than' 연산자 : '<'
		// case Constants.LESS_THAN: {
		// if (!isEmpty) {
		// if (type.equalsIgnoreCase("number")) {
		// p = cb.lessThan(root.<Long> get(name), (Long) value);
		// } else {
		// cb.lessThan(root.<String> get(name), (String) value);
		// }
		// }
		// break;
		// }
		//
		// // 'less than equal' 연산자 : '<='
		// case Constants.LESS_THAN_EQUAL: {
		// if (!isEmpty) {
		// if (type.equalsIgnoreCase("number")) {
		// p = cb.lessThanOrEqualTo(root.<Long> get(name), (Long) value);
		// } else {
		// cb.lessThanOrEqualTo(root.<String> get(name), (String) value);
		// }
		// }
		// break;
		// }
		//
		// // 'between' 연산자 : 'between'
		// case Constants.BETWEEN: {
		// if (!isEmpty) {
		// // between 값은 ','로 구분되어 넘어오게 되므로 ','로 잘라서 두 개의 값을 만든다.
		// String toValues = (String) value;
		// Object[] valueArr = toValues.split(",");
		//
		// if (valueArr.length == 2) {
		// if (type.equalsIgnoreCase("number")) {
		// cb.between(root.<Long> get(name), (Long) valueArr[0], (Long)
		// valueArr[1]);
		// } else {
		// cb.between(root.<String> get(name), (String) valueArr[0], (String)
		// valueArr[1]);
		// }
		// }
		// }
		// break;
		// }

		// 'is null' 연산자 : 'is null'
		case Constants.IS_NULL: {
			p = cb.isNull(root.get(name));
			break;
		}

		// 'is not null' 연산자 : 'is not null'
		case Constants.IS_NOT_NULL: {
			p = cb.isNotNull(root.get(name));
			break;
		}

		// 'is blank' 연산자 : 'is null or '=' '
		case Constants.IS_BLANK: {
			Predicate p1 = cb.isNull(root.get(name));
			Predicate p2 = cb.equal(root.get(name), "");
			p = cb.or(p1, p2);
			break;
		}

		// 'is present' 연산자 : 'is not null or '=' '
		case Constants.IS_PRESENT: {
			Predicate p1 = cb.isNotNull(root.get(name));
			Predicate p2 = cb.notEqual(root.get(name), "");
			p = cb.or(p1, p2);
			break;
		}

		// 'in' 연산자 : 'in' - TODO Not In
		case Constants.IN: {
			if (!isEmpty) {
				// in 값은 ','로 구분되어 넘어오게 되므로 ','로 잘라서 Array를 만든다.
				String values = (String) value;
				Object[] valueArr = StringUtils.tokenizeToStringArray(values, ",");
				p = root.get(name).in(valueArr);
			}
			break;
		}
		}

		return p;
	}

	/**
	 * Get Order Condition
	 *
	 * @param cb
	 * @param root
	 * @param cond
	 * @return
	 */
	private Order getOrderCondition(CriteriaBuilder cb, Root<?> root, SortCondition cond) {
		String name = cond.getName();
		return cond.isAscending() ? cb.asc(root.get(name)) : cb.desc(root.get(name));
	}
}