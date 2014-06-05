package com.wanda.ccs.mem.service.impl;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TActTargetService;
import com.wanda.ccs.model.TActTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.wanda.ccs.mem.dao.ITActTargetDao;

@Service
public class TActTargetServiceImpl extends BaseCrudServiceImpl<TActTarget>
		implements TActTargetService {

	@Autowired
	private ITActTargetDao dao = null;

	@Override
	public IBaseDao<TActTarget> getDao() {
		return dao;
	}

	@Override
	public PageResult<TActTarget> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TActTarget c";
		String[] whereBodies = new String[] {
				"c.name like :name",
				"c.code like :code"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TActTarget> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}
}
