package com.minu.core.sample.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minu.core.dao.JpaDaoHandler;
import com.minu.core.entity.User;
import com.minu.core.model.SearchCondition;
import com.minu.core.model.SortCondition;
import com.minu.core.system.Constants;
import com.minu.core.util.BeanUtil;
import com.minu.core.util.CrudUtil;

public class SampleBiz {

	public String create(String value) {
		User user = CrudUtil.read(User.class, "test2");

		JpaDaoHandler dao = BeanUtil.get(JpaDaoHandler.class);
		List<User> u = dao.list(new User());
		return "DDD";
	}

	public List<User> list() {
		JpaDaoHandler dao = BeanUtil.get(JpaDaoHandler.class);

		List<SearchCondition> condList = new ArrayList<SearchCondition>();
		condList.add(new SearchCondition("id", Constants.EQUAL, "test2"));

		List<SortCondition> sortList = new ArrayList<SortCondition>();
		sortList.add(new SortCondition("name", true));

		List<User> list = dao.list(User.class, condList, sortList);
		
		
		User user = new User();
		user.setId("test2");
		List<User> l = dao.list(user);

		return list;
	}
	
	public List<User> samp() {
		String a = "/com/minu/core/sample/service/sql/user.sql";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", "test");
		map.put("name", "CCCCC");
		JpaDaoHandler dao = BeanUtil.get(JpaDaoHandler.class);
		List<User> list = dao.listByPath(a, User.class, map);
		return list;
	}
}