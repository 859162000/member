package com.wanda.ccs.mem.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TActResultService;
import com.wanda.ccs.model.TActResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.wanda.ccs.mem.dao.ITActResultDao;

@Service
public class TActResultServiceImpl extends BaseCrudServiceImpl<TActResult>
		implements TActResultService {

	@Autowired
	private ITActResultDao dao = null;

	@Override
	public IBaseDao<TActResult> getDao() {
		return dao;
	}

	@Override
	public PageResult<TActResult> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TActResult c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TActResult> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}

	@Override
	public List<TActResult> getActResultsByActivityId(Long activityId) {
		if(activityId == null)
			return null;
		String hql = "select c from TActResult c where c.cmnActivityId = :activityId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityId", activityId);
		return getDao().queryHql(hql, params);
	}
}
