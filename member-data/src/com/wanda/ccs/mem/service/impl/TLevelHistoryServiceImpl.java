package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITLevelHistoryDao;
import com.wanda.ccs.mem.service.TLevelHistoryService;
import com.wanda.ccs.model.TLevelHistory;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TLevelHistoryServiceImpl extends BaseCrudServiceImpl<TLevelHistory>
		implements TLevelHistoryService {

	@Autowired
	private ITLevelHistoryDao dao;

	@Override
	public IBaseDao<TLevelHistory> getDao() {
		return dao;
	}

	@Override
	public PageResult<TLevelHistory> findByCriteria(
			QueryCriteria<String, Object> query) {
		query.setPageSize(10);
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TLevelHistory c ";
		queryParam.add("c.memberId = :memberId ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = " c.setTime desc";
		}
		PageResult<TLevelHistory> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
}
