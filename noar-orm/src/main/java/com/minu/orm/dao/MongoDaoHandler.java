package com.minu.orm.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.minu.orm.Constants;
import com.minu.orm.model.Page;
import com.minu.orm.model.SearchCondition;
import com.minu.orm.model.SortCondition;
import com.noar.util.ValueUtil;

public class MongoDaoHandler {

	@Autowired
	protected MongoTemplate mongoTemplate;

	/**
	 * Logger
	 */
	protected Logger logger = LoggerFactory.getLogger(MongoDaoHandler.class);

	public Object getKey(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public void create(Object entity) {
		this.mongoTemplate.insert(entity);
	}

	public <T> T read(T entity) {
		// TODO Key 값 조회 API 필요
		return null;
	}

	public <T> T read(Class<T> entity, Object... key) {
		return this.mongoTemplate.findById(key, entity);
	}

	public <T> T update(T entity) {
		this.mongoTemplate.save(entity);
		return entity;
	}

	public <T> T update(Class<T> entityClass, Object key) {
		// TODO Key 값 조회 API 필요
		return null;
	}

	public void delete(Object entity) {
		this.mongoTemplate.remove(entity);
	}

	public <T> void delete(Class<T> entity, Object key) {
		// TODO Key 값 조회 API 필요
	}

	public <T> List<T> list(T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> list(Class<T> entityClass, List<SearchCondition> searchConds) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> list(Class<T> entityClass, List<SearchCondition> searchConds, List<SortCondition> sortConds) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> List<T> listByPath(String path, Class<T> clazz, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> Page<T> search(T entity, int countPage, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> Page<T> search(Class<T> entityClass, int countPage, int offset, List<SearchCondition> searchConds,
			List<SortCondition> sortConds) {

		List<Order> orderList = new ArrayList<Order>();

		if (ValueUtil.isNotEmpty(sortConds)) {
			for (SortCondition order : sortConds) {
				Direction direction = ValueUtil.toBoolean(order.isAscending()) ? Direction.ASC : Direction.DESC;
				orderList.add(new Order(direction, order.getName()));
			}
		}

		int start = countPage - 1;
		// Order by
		final PageRequest pageable;
		if (ValueUtil.isEmpty(orderList)) {
			pageable = new PageRequest(start, offset);
		} else {
			pageable = new PageRequest(start, offset, new Sort(orderList));
		}

		// Where
		Query condition = new Query().with(pageable);
		// condition.addCriteria(Criteria.where("domainId").is(Domain.currentDomain().getId()));

		for (SearchCondition cond : searchConds) {
			Object value = cond.getValue();
			String operand = cond.getName();
			String operator = cond.getOperator();

			if (cond.getOperator().equals(Constants.EQUAL))
				condition.addCriteria(Criteria.where(operand).is(value));
			else if (operator.equals(Constants.NOT_EQUAL))
				condition.addCriteria(Criteria.where(operand).ne(value));
			else if (operator.equals(Constants.LIKE))
				condition.addCriteria(Criteria.where(operand).regex(String.valueOf(value)));
			else if (operator.equals(Constants.GREATER_THAN))
				condition.addCriteria(Criteria.where(operand).gt(value));
			else if (operator.equals(Constants.GREATER_THAN_EQUAL))
				condition.addCriteria(Criteria.where(operand).gte(value));
			else if (operator.equals(Constants.LESS_THAN))
				condition.addCriteria(new Criteria().andOperator(Criteria.where(operand).lt(value)));
			else if (operator.equals(Constants.LESS_THAN_EQUAL))
				condition.addCriteria(new Criteria().andOperator(Criteria.where(operand).lte(value)));
			else if (operator.equals(Constants.IN))
				condition.addCriteria(Criteria.where(operand).in(value));
		}
		return null;
	}

	/**
	 * Entity 검색
	 * 
	 * @param entityClass
	 * @param query
	 * @return
	 */
	protected <T> Page<?> search(Class<T> entityClass, Query query, PageRequest pageable) {
		long total = this.mongoTemplate.count(query, entityClass);

		List<T> list = (List<T>) this.mongoTemplate.find(query, entityClass);
		org.springframework.data.domain.Page<T> resultPage = new PageImpl<T>(list, pageable, total);

		Page<T> returnPage = new Page<T>();
		returnPage.setTotalCount(ValueUtil.toLong(resultPage.getTotalElements()));
		returnPage.setList(resultPage.getContent());

		return returnPage;
	}
}