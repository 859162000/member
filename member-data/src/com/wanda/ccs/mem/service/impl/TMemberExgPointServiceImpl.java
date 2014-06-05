package com.wanda.ccs.mem.service.impl;

import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wanda.ccs.mem.dao.ITMemberExgPointDao;
import com.wanda.ccs.mem.dao.ITMemberLevelDao;
import com.wanda.ccs.mem.service.TMemberExgPointService;
import com.wanda.ccs.mem.service.TMemberLevelService;
import com.wanda.ccs.model.TMemberExgPoint;
import com.wanda.ccs.model.TMemberLevel;
import com.xcesys.extras.core.dao.IBaseDao;
import com.xcesys.extras.core.model.PageResult;
import com.xcesys.extras.core.model.QueryCriteria;
import com.xcesys.extras.core.service.impl.BaseCrudServiceImpl;

@Service
public class TMemberExgPointServiceImpl extends BaseCrudServiceImpl<TMemberExgPoint>
		implements TMemberExgPointService {

	@Autowired
	private ITMemberExgPointDao dao;

	@Override
	public IBaseDao<TMemberExgPoint> getDao() {
		return dao;
	}

	@Override
	public PageResult<TMemberExgPoint> findByCriteria(
			QueryCriteria<String, Object> query) {
		Vector<String> queryParam = new Vector<String>();
		String fromClause = " from TMemberExgPoint c ";
		queryParam.add("c.memberId = :memberId ");
		String[] whereBodies = queryParam
				.toArray(new String[queryParam.size()]);
		String joinClause = "";
		String orderClause = " order by c.expireTime ";
		//if (query.getSort() == null || query.getSort().trim().length() == 0) {
		//	orderClause = "";
		//}
		PageResult<TMemberExgPoint> result = getDao().queryQueryCriteria(fromClause,
				joinClause, orderClause, whereBodies, query);
		return result;
	}
	
}
