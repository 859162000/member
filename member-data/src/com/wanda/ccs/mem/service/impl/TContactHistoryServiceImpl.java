package com.wanda.ccs.mem.service.impl;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TContactHistoryService;
import com.wanda.ccs.model.TContactHistory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.wanda.ccs.mem.dao.ITContactHistoryDao;

@Service
public class TContactHistoryServiceImpl extends
		BaseCrudServiceImpl<TContactHistory> implements TContactHistoryService {

	@Autowired
	private ITContactHistoryDao dao = null;

	@Override
	public IBaseDao<TContactHistory> getDao() {
		return dao;
	}

	@Override
	public PageResult<TContactHistory> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TContactHistory c";
		String[] whereBodies = new String[] {};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TContactHistory> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;

	}
}
