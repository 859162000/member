package com.wanda.ccs.mem.service.impl;

import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.service.TExtPointCriteriaService;
import com.wanda.ccs.model.TExtPointCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;
import com.wanda.ccs.mem.dao.ITExtPointCriteriaDao;

@Service
public class TExtPointCriteriaServiceImpl extends BaseCrudServiceImpl<TExtPointCriteria>
		implements TExtPointCriteriaService {

	@Autowired
	private ITExtPointCriteriaDao dao = null;

	@Override
	public IBaseDao<TExtPointCriteria> getDao() {
		return dao;
	}

	@Override
	public PageResult<TExtPointCriteria> findByCriteria(
			QueryCriteria<String, Object> query) {
		String fromClause = "from TExtPointCriteria c";
		String[] whereBodies = new String[] {
				"c.name like :name",
				"c.code like :code"
		};
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.id desc";
		}

		PageResult<TExtPointCriteria> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;

	}
}
