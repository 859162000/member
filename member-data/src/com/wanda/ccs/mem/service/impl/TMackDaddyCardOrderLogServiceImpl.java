package com.wanda.ccs.mem.service.impl;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TMackDaddyCardOrderLogService;
import com.wanda.ccs.model.TMackDaddyCardOrderLog;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.wanda.ccs.mem.dao.ITMackDaddyCardOrderLogDao;

@Service
public class TMackDaddyCardOrderLogServiceImpl extends
		BaseCrudServiceImpl<TMackDaddyCardOrderLog> implements
		TMackDaddyCardOrderLogService {

	@Autowired
	private ITMackDaddyCardOrderLogDao dao = null;

	@Override
	public IBaseDao<TMackDaddyCardOrderLog> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMackDaddyCardOrderLog> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TMackDaddyCardOrderLog c";
		String[] whereBodies = new String[] {
				"c.tMackDaddyCardOrder.id = :mackDaddyCardOrderId"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TMackDaddyCardOrderLog> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;

	}
	
}
