package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemberLevelDao;
import com.wanda.ccs.mem.service.TMemberLevelService;
import com.wanda.ccs.model.TMemberLevel;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberLevelServiceImpl extends BaseCrudServiceImpl<TMemberLevel>
		implements TMemberLevelService {

	@Autowired
	private ITMemberLevelDao dao;

	@Override
	public IBaseDao<TMemberLevel> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMemberLevel> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TMemberLevel c ";
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = null;
		if (query.getSort() == null || query.getSort().trim().length() == 0) {
			orderClause = "";
		}
		PageResult<TMemberLevel> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
}
