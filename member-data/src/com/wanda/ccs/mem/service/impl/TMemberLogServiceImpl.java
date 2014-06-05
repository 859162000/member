package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wanda.ccs.mem.dao.ITMemberLogDao;
import com.wanda.ccs.mem.service.TMemberLogService;
import com.wanda.ccs.model.TMemberLog;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberLogServiceImpl extends BaseCrudServiceImpl<TMemberLog>
		implements TMemberLogService {

	@Autowired
	private ITMemberLogDao dao;

	@Override
	public IBaseDao<TMemberLog> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMemberLog> findByCriteria(
			QueryCriteria<String, Object> query) {
		query.setPageSize(10);
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TMemberLog c ";
		queryParam.add(" c.meberId = :memberId ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "c.changedDate desc";
		}
		PageResult<TMemberLog> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		
		return result;
	}
	
}
