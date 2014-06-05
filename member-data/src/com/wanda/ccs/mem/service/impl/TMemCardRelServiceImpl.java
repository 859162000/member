package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemCardRelDao;
import com.wanda.ccs.mem.service.TMemCardRelService;
import com.wanda.ccs.model.TMemCardRel;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemCardRelServiceImpl extends BaseCrudServiceImpl<TMemCardRel>
		implements TMemCardRelService {

	@Autowired
	private ITMemCardRelDao dao;

	@Override
	public IBaseDao<TMemCardRel> getDao() {
		return dao;
	}
	@Override
	public PageResult<TMemCardRel> findByCriteria(
			QueryCriteria<String, Object> query) {
		query.setPageSize(10);
		Vector<String> queryParam = new Vector<String>();
		String fromClause = "from TMemCardRel c";
		queryParam.add(" c.memberId = :memberId ");
		queryParam.add(" c.isUnBind = 1 ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.bindTime desc";
		}
		getDao().setCacheable(false);
		PageResult<TMemCardRel> result = getDao().queryQueryCriteria(
				fromClause, joinClause, orderClause, whereBodies, query);
		return result;

	}
}
